SCALATEST_JAR = "org.scalatest:scalatest:jar:1.0"

module Buildr::Scala
  class ScalaTest
    class << self 
      def dependencies
        puts "Make sure patch outlined in http://groups.google.com/group/scalatest-users/browse_thread/thread/46a98f94b53152a8 has been applied"
        [SCALATEST_JAR] + Check.dependencies + 
          JMock.dependencies + JUnit.dependencies
      end

    end
  end
end