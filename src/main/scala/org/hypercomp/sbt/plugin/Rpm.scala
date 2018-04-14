package org.hypercomp.sbt.plugin

import sbt._
import sbt.Keys._
import complete.DefaultParsers._

import org.redline_rpm.header.Architecture
import org.redline_rpm.header.Os
import org.redline_rpm.Builder
import org.redline_rpm.header.RpmType
import org.redline_rpm.header.Flags

object Rpm extends AutoPlugin {

  override def trigger = allRequirements

  object RpmKeys {

  }

  object autoImport {

    /*
     * Tasks provided by this plugin
     */
    val rpmBuild = inputKey[Unit]("main task for rpm build")
    val rpmClean = inputKey[Unit]("main task for rpm clean")

    /*
     * Packaging options
     */
    val rpmDestinationDirectory = settingKey[String]("destinatin directory of the RPM on local file system")
    /*
     * RPM Headers
     */
    val rpmPackageName = settingKey[String](s"name of the package. defaults to project name which in this case is ${name}")
    val rpmPackageDescription = settingKey[String]("package description")
    val rpmPackageRelease = settingKey[String]("RPM Release")
    val rpmPackageVersion = settingKey[String]("RPM Version")
    val rpmEpoch = settingKey[Int]("Defaults to 0")
    val rpmUser = settingKey[String]("Default user to which file permissions are to be assigned")
    val rpmPermissionGroup = settingKey[String]("Default group to which file permissions are to be assigned")
    val rpmArch = settingKey[String](s"Architecture/Instruction set type which can be one of ${Architecture.values().mkString(",")}")
    val rpmOS = settingKey[String](s"Operating System type which can be one of ${Os.values().mkString(",")}")
    val rpmLicense = settingKey[String]("Type of license")
    val rpmPackageDependencies = settingKey[Seq[(String,String,String)]]("The package and it's version on which this RPM depends")
    val rpmDirectory = settingKey[Unit]("Directory to be added to the RPM")
    val rpmPreInstallScript = settingKey[String]("Pre-installation script")
    val rpmPreUninstallScript = settingKey[String]("Pre-UnInstallation script")
    val rpmPostInstallScript = settingKey[String]("Post-installation script")
    val rpmPostUninstallScript = settingKey[String]("Post-UnInstallation script")
    val rpmPackageFiles = taskKey[Map[String, String]]("List of files which are to be included in package")
    val rpmPackageDirectories = settingKey[Seq[String]]("List of directories which are to be included in package")
  }

  import autoImport._

  override val projectSettings = Seq(
    rpmPackageName := undefinedKeyError(rpmPackageName.key),
    rpmPackageDescription := undefinedKeyError(rpmPackageDescription.key),
    rpmPackageRelease := undefinedKeyError(rpmPackageRelease.key),
    rpmPackageVersion := undefinedKeyError(rpmPackageVersion.key),
    rpmOS := undefinedKeyError(rpmOS.key),
    rpmArch := undefinedKeyError(rpmArch.key),
    rpmPackageFiles := Map[String, String](),
    rpmPackageDirectories := Seq[String](),
    rpmPackageDependencies := Seq(),
    rpmPreInstallScript := "",
    rpmPostInstallScript := "",
    rpmPreUninstallScript := "",
    rpmPostUninstallScript := "",
    rpmBuild := {
      println("Building RPM")
      val rpmBuilder: Builder = new Builder()
      rpmBuilder.setType(RpmType.BINARY)
      rpmBuilder.setPackage(rpmPackageName.value, rpmPackageVersion.value, rpmPackageRelease.value)
      rpmBuilder.setPlatform(Architecture.valueOf(rpmArch.value.toUpperCase()), Os.valueOf(rpmOS.value.toUpperCase))
      rpmBuilder.setDescription(rpmPackageDescription.value)
      /*
       * Get list of files which are to be added and add them one by one
       */
      
      streams.value.log.info("Adding RPM Package Files")
            
      if (!rpmPackageFiles.value.isEmpty) {
        rpmPackageFiles.value.foreach { file =>
          streams.value.log.info(s"adding file : ${file._1}")
          /*
           * TODO : Check if file specified as input exists
           */
          rpmBuilder.addFile(file._2, new File(file._1))
        }
      }
      
        /*
         * Get list of directories which are to be added and add them one by one
         */
        if (!rpmPackageDirectories.value.isEmpty) {
          rpmPackageDirectories.value.foreach { dir =>
            streams.value.log.info(s"adding directory : ${dir}")
            rpmBuilder.addDirectory(dir)
          }
        }
        
        /*
         * Add dependencies
         */
        if(!rpmPackageDependencies.value.isEmpty) {
          rpmPackageDependencies.value.foreach {
            dep =>
              streams.value.log.info(s"adding dependency on ${dep._1}")
              rpmBuilder.addDependency(dep._1, Flags.GREATER | Flags.EQUAL, dep._3)
          }
        }
        
        /*
         * Add Pre/Post install scripts
         */
        streams.value.log.info("Checking pre-install script")
        if(!rpmPreInstallScript.value.isEmpty) {
          streams.value.log.info(s"Setting pre-install script ${rpmPreInstallScript.value}")
          rpmBuilder.setPreInstallScript(file(rpmPreInstallScript.value))
        }     
        
        streams.value.log.info("Checking post-install script")
        if(!rpmPostInstallScript.value.isEmpty) {
          streams.value.log.info(s"Setting post-install script ${rpmPostInstallScript.value}")
          rpmBuilder.setPostInstallScript(file(rpmPostInstallScript.value))
        }
        
        streams.value.log.info("Checking pre-un-install script")
        if(!rpmPreUninstallScript.value.isEmpty) {
          println(s"Setting pre-un-install script ${rpmPreUninstallScript.value}")
          rpmBuilder.setPreUninstallScript(file(rpmPreUninstallScript.value))
        }
        
        streams.value.log.info("Checking post-un-install script")
        if(!rpmPostUninstallScript.value.isEmpty) {
          streams.value.log.info(s"Setting post-un-install script ${rpmPostUninstallScript.value}")
          rpmBuilder.setPostUninstallScript(file(rpmPostUninstallScript.value))
        }
        
      
      rpmBuilder.build(new File(rpmDestinationDirectory.value))
      streams.value.log.success("Finished building RPM")
    },
    rpmClean := {
      val rpmFileName = rpmDestinationDirectory.value + "/" + rpmPackageName.value + "-" + rpmPackageVersion.value + "-" + rpmPackageRelease.value + "." + rpmArch.value.toLowerCase + ".rpm";
      streams.value.log.info(s"Cleaning RPM file : ${rpmFileName} ")      
      IO.delete(new File(rpmFileName))
    })

  def undefinedKeyError[A](key: AttributeKey[A]): A = {
    sys.error(
      s"${key.description.getOrElse("A required key")} is not defined. " +
        s"Please declare a value for the `${key.label}` key.")
  }
}