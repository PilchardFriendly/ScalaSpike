require 'buildr'

repositories.local = 'lib/maven/repository'
require 'buildr/scala'
repositories.remote = []


require 'src/build/ruby/libraries.rb'

VERSION_NUMBER = '1.0'

define "ScalaSpike" do
  project.version = VERSION_NUMBER

  define "Haml" do
    package :jar
  end
end