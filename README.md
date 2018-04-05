# sbt-rpm
* SBT Plugin using the Redline RPM library to build RPMs. 
* The motivation behind this custom auto-plugin is to provide RPM creation capability from SBT using a purely Java approach i.e. without using external tools such as ```rpm``` and ```rpmbuild```.  
* Thus, a user of this plugin can also build an RPM on a Windows/non-Linux machine, provided it meets the basic requirements below.

## Pre-requisites
* Scala version >= 2.10
* sbt version >= 0.13.13
[Back to Top](#sbt-rpm)

## Getting it
* The current stable version is 1.0.3
* Add the plugin to your project or global plugins.sbt file
```shell
addSbtPlugin("org.hypercomp" % "sbt-rpm" % "1.0.3")
```
* The plugin is hosted in [bintray](https://bintray.com)

[Back to Top](#sbt-rpm)

## Plugin Mappings
### Tasks (*TaskKey*'s)
This plugin provides 2 tasks. Both are to be invoked without any additional command-line arguments
* rpmBuild
* rpmClean

### Settings (*SettingKey*'s)
The RPM spec needs a number of headers/parameters to be provided to build the RPM package.
These are all specified using the *SettingKey*'s of the plugin  
  
| SettingKey | Description |  
|---|---| 
| rpmPackageName | Name of the package | 
| rpmPackageDescription |  | 
| rpmPackageRelease |  | 
| rpmPackageVersion |  | 
| rpmEpoch |  | 
| rpmUser |  | 
| rpmPermissionGroup |  | 
| rpmArch |  | 
| rpmOS |  | 
| rpmLicense |  | 
| rpmPackageDependencies |  | 
| rpmDirectory |  | 
| rpmPreInstallScript |  | 
| rpmPreUninstallScript |  | 
| rpmPostInstallScript |  | 
| rpmPostUninstallScript |  | 
| rpmPackageFiles |  | 
| rpmPackageDirectories |  | 


[Back to Top](#plugin-mappings)