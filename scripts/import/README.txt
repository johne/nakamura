This import process requires a working "sendmail"

It also requires the "pony" gem installed

sudo gem install pony



it also requires the nakamura gem

sudo gem install nakamura



Alternatively, you can install the 'bundler' gem and then have it install the dependencies for you:

gem install bundler
bundle install



to import users call:

./import-users <path to csv file> <path to settings file>

where "settings file" is a json file formatted like the sampleServer.json

to remove users call:

./remove-users.rb <path to csv file> <path to settings file>
 


to provision users into worlds, call:

./provision-worlds.rb <path to csv file> <path to settings file>

where "settings file" is the same as above
