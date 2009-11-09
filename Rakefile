task :default => [:ci]

task :ci do
  puts %x{buildr clean package}
  raise "failed: buildr clean package with exit code #{$?}" unless $?.success?
end
