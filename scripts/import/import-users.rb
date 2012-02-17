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


$ALLOW_UPDATE = true
$UPDATE_PASSWORD = false # always false

if ARGV.size < 1
    puts "Usage: import-users.rb PATH_TO_CSV_FILE [PATH_TO_SERVER_CONFIG_FILE.json]"
    exit 1
end


class SisUserUploader < OaeImportBase

    attr_accessor :allow_update
    attr_accessor :customProperties

    def initialize(serverInfoFile = nil)
        super(serverInfoFile)
        @allow_update = $ALLOW_UPDATE
    end

    def createManager(server)
        @userManager = UserManager.new(server)
    end

    def processRow(row)

        # 0 - EID
        # 1 - last name
        # 2 - first name
        # 3 - email
        # 4 - password
        # 5 - user type
        # 6 - internal id
        # 7, etc - properties

        user = User.new(row[0])
        user.lastName = row[1]
        user.firstName = row[2]
        user.email = row[3]
        user.password = row[4]

        props = @userManager.get_user_props(user.name)

        processed = false

        if (props["name"].nil?)
            # not found... create
            user = @userManager.create_user_object(user)
            @created += 1
            processed = true
        else
            #found... update
            if (allow_update)
                user.update_user(@server)
                @updated += 1
                processed = true
            end
        end

        if user && processed
            props = {"cle_userType"=>row[5]}

            if (!customProperties.nil? && customProperties.count > 0)

                customProperties.each_with_index {
                    |name, index|

                    props[name] = row[7 + index]
                }
            end

            user.update_properties(@server, props)
        end
    end

    def processServerProps(serverInfoFile)

        ret = super(serverInfoFile)

        @allow_update = @serverProps["users.csv"]["allowUpdate"] || $ALLOW_UPDATE

        @customProperties = @serverProps["users.csv"]["customProperties"]

        ret
    end

    def subject
      "users.csv file processed: " + currentDate
    end

    def skipFirstRow
      skipFirstRowConfig("users.csv")
    end

    def expectedColumns
      if (!customProperties.nil?)
        return 7 + customProperties.count
      else 
        return 7
      end
    end

end


csvFile = ARGV[0]
serverInfo = ARGV[1] || nil
@sisUser = SisUserUploader.new(serverInfo)

@sisUser.processFile(csvFile)
