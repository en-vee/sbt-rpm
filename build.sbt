lazy val rpmBuild = taskKey[Unit]("Task to build an RPM")

lazy val rpmVerify = taskKey[Unit]("Task to verify an RPM")

lazy val root = (project in file(".")).settings(
			name := "sbt-rpm",
			version := "1.0.0-SNAPSHOT",
			organization := "org.hypercomp",
			sbtPlugin := true,
			libraryDependencies += "org.redline-rpm" % "redline" % "1.2.6"
		).settings(
			scriptedSettings: _*
		).settings(
			scriptedLaunchOpts ++= Seq("-Dplugin.version=" + version.value),
			scriptedBufferLog := false
		)

//initialCommands in console := "import org.hypercomp.sbt.plugins._"
