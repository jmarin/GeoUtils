name := "GeoUtils"

version := "1.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers ++= {
  Seq(
    "OSGeo Repository" at "http://download.osgeo.org/webdav/geotools/",
    "OpenGeo Repository" at "http://repo.opengeo.org/"
  )	  
}


libraryDependencies ++= {
  Seq(
    "junit" % "junit" % "4.8" % "test",
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    "org.specs2" %% "specs2" % "1.12.2" % "test"    
  )		   
}


libraryDependencies ++= {
  val geotoolsVersion = "9-SNAPSHOT"
  Seq(
   	"org.geotools" % "gt-main" % geotoolsVersion % "compile->default",
    	"org.geotools" % "gt-shapefile" % geotoolsVersion % "compile->default",
    	"org.geotools" % "gt-epsg-hsql" % geotoolsVersion % "compile->default"
    )
}

publishTo := Some(Resolver.file("Local Maven Repository", new java.io.File(Path.userHome.absolutePath + "/.m2/repository")))
