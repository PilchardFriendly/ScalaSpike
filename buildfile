require 'buildr'

maven_repo = File.join(File.expand_path(File.dirname(__FILE__)), "lib", "maven", "repository")
repositories.local = maven_repo
require 'buildr/scala'

require 'src/build/ruby/libraries.rb'

VERSION_NUMBER = '1.0'

define "ScalaSpike" do
  project.version = VERSION_NUMBER

  define "Haml" do
    package :jar
  end
end