name := "GeoUtils"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "Open Source Geospatial Foundation Repository" at "http://download.osgeo.org/webdav/geotools/"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1" % "test"

libraryDependencies ++= {
  val geotoolsVersion = "8.0-M3"
  Seq(
   	"org.geotools" % "gt-main" % geotoolsVersion % "compile->default",
    	"org.geotools" % "gt-shapefile" % geotoolsVersion % "compile->default",
    	"org.geotools" % "gt-epsg-hsql" % geotoolsVersion % "compile->default"
    )
}

publishTo := Some(Resolver.file("Local Maven Repository", new java.io.File(Path.userHome.absolutePath + "/.m2/repository")))
