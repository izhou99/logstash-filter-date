#!/usr/bin/env ruby

$: << File.join(Dir.pwd, "lib")

def install_gem(name, requirement)
  require "rubygems/commands/install_command"
  installer = Gem::Commands::InstallCommand.new
  installer.options[:generate_rdoc] = false
  installer.options[:generate_ri] = false
  installer.options[:version] = requirement
  installer.options[:args] = [name]
  installer.options[:install_dir] = ENV["GEM_HOME"]
  installer.options[:document] = []

  begin
    gem name, requirement
  rescue Gem::LoadError => e
    puts "Failed to load #{name}. Will try to install"
    installer.execute
    Gem::Specification.reset
    gem name, requirement
  end
end

def bundler()
  install_gem("bundler", "~> 1.9.4")
  install_gem("pry", ">0")
  puts("Invoking bundler install...")
  require "bundler/cli"
  Gem::Specification.reset
  Bundler::reset!
  Bundler::CLI.start([ "install" ])
rescue => e
  require "rubygems/commands/environment_command"
  cmd = Gem::Commands::EnvironmentCommand.new
  cmd.execute
  raise e
end