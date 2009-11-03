

require 'buildr'
require 'buildr/scala'

require 'src/build/ruby/libraries.rb'

VERSION_NUMBER = '1.0'

define "ScalaSpike" do
  project.version = VERSION_NUMBER

  define "Haml" do
    package :jar
  end
end