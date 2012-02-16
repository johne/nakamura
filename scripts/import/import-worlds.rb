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


if ARGV.size < 1
    puts "Usage: import-worlds.rb PATH_TO_CSV_FILE [PATH_TO_SERVER_CONFIG_FILE.json]"
    exit 1
end


class SisWorldUploader < OaeImportBase
  
  attr_accessor :customProperties
  attr_accessor :globalGrouping
  attr_accessor :worldTemplateMap
  
  def createManager(server)
      @userManager = UserManager.new(server)
      @fullGroupCreator = FullGroupCreator.new(server)
  end
  
  
  def processRow(row)
    # 0 term (set as property)
    # 1 world id (groupname)
    # 2 start date (not used)
    # 3 title
    # 4 description
    # 5 long description
    # 6 published (what to do with this)
    # 7 end date (not used)
    # 8 world template mapping
    # 9 contact name (set as property)
    # 10 contact email (set as property)
    # 11 grouping (set as property... if missing, use globalGrouping)
    # 12 ... 15 (set as property based on @customProperties)
    
    # create or load group
    group = Group.new(row[1]);
    groupDetails = createOrLoad(group, row);
    
    if (groupDetails["properties"].nil?)
      raise "failed to create world"
    end
    
    # update properties based on above
    grouping = row[11]
    
    if (grouping.nil?)
      grouping = @globalGrouping
    end
    
    groupProps = {
      "term" => row[0],
      "contactName" => row[9],
      "contactEmail" => row[10],
      "grouping" => grouping
    }
    
    groupDetails["properties"]["term"] = row[0]
    groupDetails["properties"]["contactName"] = row[9]
    groupDetails["properties"]["contactEmail"] = row[10]
    groupDetails["properties"]["grouping"] = grouping

    if (!@customProperties.nil? && @customProperties.count > 1)
        @customProperties.each_with_index {
            |name, index|
            if (index > 0)
              groupProps[name] = row[11 + index]
            end
        }
    end

    group.update_properties(@server, groupProps)
  end
  
  def createOrLoad(group, row) 
    groupDetails = group.details(@server)
    
    if (!groupDetails["properties"].nil?)
      @updated += 1
      return groupDetails
    end
    
    # create group here
    visibility = "private"
    
    if (row[6] = '1') 
      visibility = "public"
    end
    
    data = JSON.generate({
      "id" => row[1],
      "title" => row[3],
      "description" => row[5],
      "joinability" => "no",
      "visibility" => visibility,
      "tags" => [],
      "worldTemplate" => @worldTemplateMap[row[8]],
      "_charset_" => "utf-8"
    }) 
    
    @fullGroupCreator.create_full_group_with_data(row[1], data)
    
    @created += 1
    
    return group.details(@server)    
  end
  
  def processServerProps(serverInfoFile)
      ret = super(serverInfoFile)

      @customProperties = @serverProps["worlds.csv"]["customProperties"] # should at least include the "grouping" param
      @worldTemplateMap = @serverProps["worlds.csv"]["worldTemplateMap"] # maps from CLE site types to world template paths
      @globalGrouping = @serverProps["worlds.csv"]["globalGrouping"] # determine if grouping should be on by default (or override?)

      ret
  end
  
  def subject
    "worlds.csv file processed: " + currentDate
  end

  def skipFirstRow
    skipFirstRowConfig("worlds.csv")
  end

  def expectedColumns
    11 + customProperties.count
  end

end

csvFile = ARGV[0]
serverInfo = ARGV[1] || nil
@sisWorld = SisWorldUploader.new(serverInfo)

@sisWorld.processFile(csvFile)
