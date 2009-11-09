SCALATEST_JAR = "org.scalatest:scalatest:jar:1.0"

module Buildr::Scala
  class ScalaTest
    class << self 
      def dependencies
        [SCALATEST_JAR] + Check.dependencies +
          JMock.dependencies + JUnit.dependencies
      end
    end

        puts "Applying patch outlined in http://groups.google.com/group/scalatest-users/browse_thread/thread/46a98f94b53152a8"

        def run(scalatest, dependencies) #:nodoc:
          mkpath task.report_to.to_s
          success = []

          reporter_options = 'TFGBSAR' # testSucceeded, testFailed, testIgnored, suiteAborted, runStopped, runAborted, runCompleted
          scalatest.each do |suite|
            info "ScalaTest #{suite.inspect}"
            # Use Ant to execute the ScalaTest task, gives us performance and reporting.
            reportFile = File.join(task.report_to.to_s, "TEST-#{suite}.txt")
            taskdef = Buildr.artifacts(self.class.dependencies).each(&:invoke).map(&:to_s)
            Buildr.ant('scalatest') do |ant|
              ant.taskdef :name=>'scalatest', :classname=>'org.scalatest.tools.ScalaTestTask',
                :classpath=>taskdef.join(File::PATH_SEPARATOR)
              ant.scalatest :runpath=>dependencies.join(File::PATH_SEPARATOR) do
                ant.suite    :classname=>suite
                ant.reporter :type=>'stdout', :config=>reporter_options
                ant.reporter :type=>'file', :filename=> reportFile, :config=>reporter_options
                # TODO: This should be name=>value pairs!
                #ant.includes group_includes.join(" ") if group_includes
                #ant.excludes group_excludes.join(" ") if group_excludes
                (options[:properties] || []).each { |name, value| ant.config :name=>name, :value=>value }
              end
            end

            # Parse for failures, errors, etc.
            # This is a bit of a pain right now because ScalaTest doesn't flush its
            # output synchronously before the Ant test finishes so we have to loop
            # and wait for an indication that the test run was completed.
            failed = false
            completed = false
            wait = 0
            while (!completed) do
              File.open(reportFile, "r") do |input|
                while (line = input.gets) do
                  failed = (line =~ /(TESTS? FAILED -)|(RUN STOPPED)|(RUN ABORTED)/) unless failed
                  completed |= (line =~ /(Run completed\.)|(All tests passed\.)/)
                  break if (failed)
                end
              end
              wait += 1
              break if (failed || wait > 10)
              unless completed
                sleep(1)
              end
            end
            success << suite if (completed && !failed)
          end

          success
        end # run
    
  end
end