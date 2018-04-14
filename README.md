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
  
| SettingKey | Type | Description |  Mandatory/Optional (M/O) | Default Value  (if Optional) | Comments/Examples |
|---|---|---|---|---|---|
| rpmPackageName | String | Name part of the package | M | | An RPM package full name is of the format \<rpmPackageName\>-\<rpmRelease\>-\<rpmVersion\>-\<rpmArch\>\.rpm |
| rpmPackageDescription | String | Description of the package | O | Null | Any free text |
| rpmPackageRelease | String | Release number which is usually an integer | O | 1 | |
| rpmPackageVersion | String | Version such as "1.0.0" | M | | | 
| rpmEpoch | Int | Epoch | O | 0 | | 
| rpmUser | String | User to assign permission for installed files/directories | O | | | 
| rpmPermissionGroup | String | User group to assign permission for installed files/directories | O | | |
| rpmArch | String | CPU Architecture. Valid Values are : NOARCH, I386, ARM, X86_64 | M | | | 
| rpmOS | String | Operating System. Value Values : LINUX, WINDOWS, etc. | 
| rpmLicense |  String | | | | | 
| rpmPackageDependencies | Seq[(packageName: String, comparisionOperator : String, packageVersion : String)] | equates to the requires directive in an RPM SPEC file. This is to be specified as a sequence. At the time of writing, a comparision operator of >= is hardcoded | O |  | | 
| rpmPackageDirectories | Seq[String] | List of directories to be created as part of the RPM installation | O | | |
| rpmPreInstallScript | String | Full path to the RPM pre-installation-script | O | | |
| rpmPreUninstallScript | String | Full path to the RPM pre-un-installation-script | O | | | 
| rpmPostInstallScript | String | Full path to the RPM post-installation-script | O | | |
| rpmPostUninstallScript | String | Full path to the RPM post-un-installation-script | O | | |
| rpmPackageFiles | Map[String, String] | HashMap where Keys consist of the Source of the files which are to be packaged in the RPM and the Value represent the Destination path/name of such a file | | | | 
| rpmPackageDirectories | String | | | | | 


[Back to Top](#plugin-mappings)