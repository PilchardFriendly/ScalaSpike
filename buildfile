require 'src/build/ruby/repositories.rb'

require 'buildr'
require 'buildr/scala'

VERSION_NUMBER = '1.0'

define "ScalaSpike" do
  project.version = VERSION_NUMBER

  define "Haml" do
    package :jar
  end
end