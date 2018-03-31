package org.hypercomp.sbt.plugin

import sbt._
import sbt.Keys._
import complete.DefaultParsers._

import org.redline_rpm.header.Architecture
import org.redline_rpm.header.Os
import org.redline_rpm.Builder
import org.redline_rpm.header.RpmType

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
    val destinationDirectory = settingKey[String]("destinatin directory of the RPM on local file system")
    /*
     * RPM Headers
     */
    val packageName = settingKey[String](s"name of the package. defaults to project name which in this case is ${name}")
    val packageDescription = settingKey[String]("package description")
    val packageRelease = settingKey[String]("RPM Release")
    val packageVersion = settingKey[String]("RPM Version")
    val epoch = settingKey[Int]("Defaults to 0")
    val user = settingKey[String]("Default user to which file permissions are to be assigned")
    val permissionGroup = settingKey[String]("Default group to which file permissions are to be assigned")
    val arch = settingKey[String](s"Architecture/Instruction set type which can be one of ${Architecture.values().mkString(",")}")
    val os = settingKey[String](s"Operating System type which can be one of ${Os.values().mkString(",")}")
    val license = settingKey[String]("Type of license")
    val requires = settingKey[Unit]("The package and it's version on which this RPM depends")
    val directory = settingKey[Unit]("Directory to be added to the RPM")
    val preInstallScript = settingKey[String]("Pre-installation script")
    val preUninstallScript = settingKey[String]("Pre-UnInstallation script")
    val postInstallScript = settingKey[String]("Post-installation script")
    val postUninstallScript = settingKey[String]("Post-UnInstallation script")
  }
  
  import autoImport._

  override val projectSettings = Seq(    
    packageName := undefinedKeyError(packageName.key),
    packageDescription := undefinedKeyError(packageDescription.key),
    packageRelease := undefinedKeyError(packageRelease.key),
    packageVersion := undefinedKeyError(packageVersion.key),
    os := undefinedKeyError(os.key),
    arch := undefinedKeyError(arch.key),
    rpmBuild := {
      
      println("Building RPM")      
      val rpmBuilder: Builder = new Builder()
      rpmBuilder.setType(RpmType.BINARY)
      rpmBuilder.setPackage(packageName.value, packageVersion.value, packageRelease.value)
      rpmBuilder.setPlatform(Architecture.valueOf(arch.value.toUpperCase()), Os.valueOf(os.value.toUpperCase))
      rpmBuilder.build(new File(destinationDirectory.value))
    },
    rpmClean := {
      println("Cleaning RPM")
      IO.delete(new File(destinationDirectory.value + "/" + packageName.value + "-" + packageVersion.value + "-" + arch.value))
    }
    
  )

  def undefinedKeyError[A](key: AttributeKey[A]): A = {
    sys.error(
      s"${key.description.getOrElse("A required key")} is not defined. " +
        s"Please declare a value for the `${key.label}` key.")
  }
  
  private [this] def buildRpm() {
  }
  
  private [this] def cleanRpm() {
    
  }
  
  private [this] def inspectRpm() {
    
  }

}