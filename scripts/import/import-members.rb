#!/usr/bin/env ruby

require 'rubygems'
require 'csv'
require 'json'
require 'nakamura'
require 'nakamura/users'
require './import-base'
include SlingInterface
include SlingUsers
include OaeImport


if ARGV.size < 1
    puts "Usage: import-members.rb PATH_TO_CSV_FILE [PATH_TO_SERVER_CONFIG_FILE.json]"
    exit 1
end


class SisWorldUploader < OaeImportBase
  
  attr_accessor :roleMaps
  attr_accessor :groupCache
  
  def createManager(server)
      @userManager = UserManager.new(server)
      @groupCache = {}
  end
  
  
  def processRow(row)
    # 0 user
    # 1 world id (groupname)
    # 2 role
    
    groupId = row[1].to_s
    
    groupCacheEntry = @groupCache[groupId];
    
    if (groupCacheEntry.nil?)
      group = Group.new(groupId);

      groupDetails = group.details(@server);
       
      groupCacheEntry = {
        "group"=> group,
        "groupDetails"=> groupDetails,
        "roles"=> Hash.new()
      }
    end
    
    worldType = groupCacheEntry["groupDetails"]["properties"]["sakai:world-type"]
    
    groupRoleMap = @roleMaps[worldType]
    
    role = groupRoleMap[row[2].to_s]
    
    roleArray = groupCacheEntry["roles"][role]
    
    if (roleArray.nil?)
      roleArray = Array.new()
    end
    
    user = row[0];

    roleArray << user
    
    # cache all the stuff away for now
    groupCacheEntry["roles"][role] = roleArray
    
    @groupCache[groupId] = groupCacheEntry
  end

  def postProcess() 
    @groupCache.each_pair do |k,v|
      begin
        processGroup(k, v)
      rescue Exception => e
        @exceptional += 1
        @log.warn(e.message)
        @log.warn(e.backtrace.join("\n"))
        exceptions << "Processing group had error: #{e.message}"
        exceptions << "\n"
      end
    end    
  end
  
  def processGroup(groupId, groupCacheEntry)
    group = Group.new(groupId)
    rolesJson = groupCacheEntry["groupDetails"]["properties"]["sakai:roles"];
    
    roles = JSON.parse(rolesJson)

    roles.each do |role|
      processRole(role["id"], group, groupCacheEntry)
    end
  end
  
  def processRole(roleId, group, groupCacheEntry) 
    newRoleUsers = groupCacheEntry["roles"][roleId]
    
    if (!newRoleUsers.nil?)
      roleGroupId = group.name + "-" + roleId
    
      roleGroup = Group.new(roleGroupId)
    
      currentMembers = roleGroup.members(@server)
    
      if (currentMembers.count > 0) 
        leftOver = currentMembers - newRoleUsers
    
        leftOver.each do |removeUser|
          roleGroup.remove_member(@server, removeUser)
        end
    
        @updated += leftOver.count
      end 
    
      if (currentMembers.count > 0)
        newRoleUsers = newRoleUsers - currentMembers
      end
    
      newRoleUsers.each do |addUser|
        roleGroup.add_member_viewer(@server, addUser)
        @created += 1
      end
    end
  end
  
  def processServerProps(serverInfoFile)
      ret = super(serverInfoFile)

      @roleMaps = @serverProps["members.csv"]["roleMaps"] # maps cle roles to oae roles

      ret
  end
  
  def subject
    "members.csv file processed: " + currentDate
  end

  def updatedLabel
    return "removed"
  end

  def skipFirstRow
    skipFirstRowConfig("members.csv")
  end

  def expectedColumns
    3
  end

end

csvFile = ARGV[0]
serverInfo = ARGV[1] || nil
@sisWorld = SisWorldUploader.new(serverInfo)

@sisWorld.processFile(csvFile)
