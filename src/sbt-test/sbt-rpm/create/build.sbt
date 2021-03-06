name := "rpm-build"
rpmOS := "linux"
rpmArch := "X86_64"
rpmDestinationDirectory := "C:/Temp"
rpmPackageVersion := "1.0.0"
rpmPackageName := "rpm-build"
rpmPackageDescription := "rpm-build-descr"
rpmPackageRelease := "1"
//rpmPackageFiles += ("C:/temp/cascade.gif" -> "/opt/axlrate/imgs/cascade.gif")
//rpmPackageFiles += ("C:/temp/prev.gif" -> "/opt/axlrate/imgs/prev.gif")
rpmPackageDirectories += "/var/opt/axlrate/log"
rpmPackageDependencies += ("axlrate-utils","gt","1.0")
rpmPreInstallScript := "src/dist/rpm/scripts/preInstall.sh"
rpmPostInstallScript := "src/dist/rpm/scripts/postInstall.sh"
exportJars := true
rpmPackageFiles :=  {
{for(f <- (fullClasspath in Runtime).value.map(_.data).filter(_.isFile()).toList) yield {
        (f.getAbsolutePath, "/opt/axlrate/lib/" + f.getName)
      }}.toMap	
}

