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
    val packageDependencies = settingKey[Seq[(String,String,String)]]("The package and it's version on which this RPM depends")
    val directory = settingKey[Unit]("Directory to be added to the RPM")
    val preInstallScript = settingKey[String]("Pre-installation script")
    val preUninstallScript = settingKey[String]("Pre-UnInstallation script")
    val postInstallScript = settingKey[String]("Post-installation script")
    val postUninstallScript = settingKey[String]("Post-UnInstallation script")
    val packageFiles = settingKey[Map[String, String]]("List of files which are to be included in package")
    val packageDirectories = settingKey[Seq[String]]("List of directories which are to be included in package")
    
  }

  import autoImport._

  override val projectSettings = Seq(
    packageName := undefinedKeyError(packageName.key),
    packageDescription := undefinedKeyError(packageDescription.key),
    packageRelease := undefinedKeyError(packageRelease.key),
    packageVersion := undefinedKeyError(packageVersion.key),
    os := undefinedKeyError(os.key),
    arch := undefinedKeyError(arch.key),
    packageFiles := Map[String, String](),
    packageDirectories := Seq[String](),
    packageDependencies := Seq(),
    preInstallScript := "",
    postInstallScript := "",
    preUninstallScript := "",
    postUninstallScript := "",
    rpmBuild := {
      println("Building RPM")
      val rpmBuilder: Builder = new Builder()
      rpmBuilder.setType(RpmType.BINARY)
      rpmBuilder.setPackage(packageName.value, packageVersion.value, packageRelease.value)
      rpmBuilder.setPlatform(Architecture.valueOf(arch.value.toUpperCase()), Os.valueOf(os.value.toUpperCase))
      rpmBuilder.setDescription(packageDescription.value)
      /*
       * Get list of files which are to be added and add them one by one
       */
      if (!packageFiles.value.isEmpty) {
        packageFiles.value.foreach { file =>
          println(s"adding file : ${file._1}")
          /*
           * TODO : Check if file specified as input exists
           */
          rpmBuilder.addFile(file._2, new File(file._1))
        }

        /*
         * Get list of directories which are to be added and add them one by one
         */
        if (!packageDirectories.value.isEmpty) {
          packageDirectories.value.foreach { dir =>
            println(s"adding directory : ${dir}")
            rpmBuilder.addDirectory(dir)
          }
        }
        
        /*
         * Add dependencies
         */
        if(!packageDependencies.value.isEmpty) {
          packageDependencies.value.foreach {
            dep =>
              streams.value.log.info(s"adding dependency on ${dep._1}")
              rpmBuilder.addDependency(dep._1, Flags.GREATER | Flags.EQUAL, dep._3)
          }
        }
        
        /*
         * Add Pre/Post install scripts
         */
        println("Checking pre-install script")
        if(!preInstallScript.value.isEmpty) {
          println(s"Setting pre-install script ${preInstallScript.value}")
          rpmBuilder.setPreInstallScript(file(preInstallScript.value))
        }     
        
        println("Checking post-install script")
        if(!postInstallScript.value.isEmpty) {
          println(s"Setting post-install script ${postInstallScript.value}")
          rpmBuilder.setPostInstallScript(file(postInstallScript.value))
        }
        
        println("Checking pre-un-install script")
        if(!preUninstallScript.value.isEmpty) {
          println(s"Setting pre-un-install script ${preUninstallScript.value}")
          rpmBuilder.setPreUninstallScript(file(preUninstallScript.value))
        }
        
        println("Checking post-un-install script")
        if(!postUninstallScript.value.isEmpty) {
          println(s"Setting post-un-install script ${postUninstallScript.value}")
          rpmBuilder.setPostUninstallScript(file(postUninstallScript.value))
        }
        
      }
      rpmBuilder.build(new File(destinationDirectory.value))
    },
    rpmClean := {
      val rpmFileName = destinationDirectory.value + "/" + packageName.value + "-" + packageVersion.value + "-" + packageRelease.value + "." + arch.value.toLowerCase + ".rpm";
      println(s"Cleaning RPM file : ${rpmFileName} ")      
      IO.delete(new File(rpmFileName))
    })

  def undefinedKeyError[A](key: AttributeKey[A]): A = {
    sys.error(
      s"${key.description.getOrElse("A required key")} is not defined. " +
        s"Please declare a value for the `${key.label}` key.")
  }

  private[this] def setScripts() {
  }

  private[this] def cleanRpm() {

  }

  private[this] def inspectRpm() {

  }

}