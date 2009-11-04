task :default => [:ci]

task :ci do
  puts "Running buildr clean package"
  gem install buildr
  puts %x{buildr clean package}
end
