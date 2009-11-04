task :default => [:ci]

task :ci do
  puts "Installing buildr gem"
  puts %x{gem install buildr --version 1.3.5}
  puts "Running buildr clean package"
  puts %x{buildr clean package}
end
