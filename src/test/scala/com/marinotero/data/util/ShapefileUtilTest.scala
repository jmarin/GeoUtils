package com.marinotero.data.util
import java.io.File

import org.geotools.data.simple.SimpleFeatureStore

import com.vividsolutions.jts.geom.Geometry

import org.opengis.feature.simple.SimpleFeature
import org.geotools.data.Query
import org.junit._
import Assert._
import scala.collection.JavaConversions._

class ShapefilUtilTest {

  val pathSeparator = System.getProperty("file.separator")

  @Test
  def testReadShapefile() {
    val shp = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "state2000gen.shp"
    val shpUtil = new ShapefileUtil()
    val featureSource = shpUtil.readLayer(shp)
    assertNotNull(featureSource)
    val count = featureSource.getCount(Query.ALL)
    assertEquals(count, 273)

  }

  @Test
  def writeShapefile() {
    val shp = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "state2000gen.shp"
    val shpUtil = new ShapefileUtil()
    val featureSource = shpUtil.readLayer(shp)
    val schema = featureSource.getSchema()
    val featureCollection = featureSource.getFeatures()
    val path = System.getProperty("user.dir") + pathSeparator + "target" + pathSeparator + "test.shp"
    shpUtil.createLayer(path, schema, featureCollection)
  }

  @Test
  def testValidShapefile() {
    val shp = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "state2000gen.shp"
    val shpUtil = new ShapefileUtil()
    val featureSource = shpUtil.readLayer(shp)
    val featureCollection = featureSource.getFeatures()
    val validFeatures = shpUtil.getValidFeaturesArray(featureCollection)
    assertEquals(validFeatures.size, 273)
  }

}