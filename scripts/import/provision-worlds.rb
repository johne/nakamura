#!/usr/bin/env ruby

require 'rubygems'
require 'csv'
require 'json'
require 'nakamura'
require 'nakamura/users'
require 'nakamura/full_group_creator'
require './import-base'
include SlingInterface
include SlingUsers
include OaeImport

class SisWorldProvisioner < OaeImportBase

  def createManager(server)
    @s = server
    @userManager = UserManager.new @s
  end

  def processRow(row)
    # 0 - EID
    # 1 - world_id
    # 2 - role_id

    user_id = row[0]
    world_id = row[1]
    role_id = row[2]
    role_group_id = world_id + "-" + role_id

    # check to see that world exists
    world = @userManager.get_group_props world_id
    role = @userManager.get_group_props role_group_id

    if world.empty? || role.empty?
      # bail, as we can't add a user to a world that doesn't exist
      @log.error "Tried to provision '#{user_id}' as a '#{role_id}' to world #{world_id}, which doesn't exist"
      @exceptional += 1
      return
    end

    group = Group.new(role_group_id)
    unless group.has_member(@s, user_id)
      @log.info "Adding #{user_id} as a #{role_id} to #{world_id}"
      group.add_member_viewer(@s, user_id)
      @updated +=1
    end
  end

  def subject
    return "worlds.csv file processed: " + currentDate
  end

  def skipFirstRow
    return skipFirstRowConfig("worlds.csv")
  end

end

if ARGV.size < 1
  puts "Usage: provision_users.rb PATH_TO_CSV_FILE [PATH_TO_SERVER_CONFIG_FILE.json]"
  exit 1
end

csvFile = ARGV[0]
serverInfo = ARGV[1] || nil
@sisProvisioner = SisWorldProvisioner.new serverInfo
@sisProvisioner.processFile csvFile
