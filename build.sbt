lazy val commonSettings = Seq(
  version in ThisBuild := "1.0.2",
  organization in ThisBuild := "org.hypercomp"
)

lazy val root = (project in file(".")).settings(
			name := "sbt-rpm",
			version := "1.0.2",
			organization := "org.hypercomp",
			sbtPlugin := true,
			libraryDependencies += "org.redline-rpm" % "redline" % "1.2.6"			
		).settings(
			scriptedSettings: _*
		).settings(
			scriptedLaunchOpts ++= Seq("-Dplugin.version=" + version.value),
			scriptedBufferLog := false
		).settings(
			licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
			publishMavenStyle := false,
			bintrayRepository := "sbt-hypercomp",
			bintrayOrganization in bintray := None			
		)

//initialCommands in console := "import org.hypercomp.sbt.plugins._"
