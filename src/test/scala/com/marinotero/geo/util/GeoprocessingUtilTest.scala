package com.marinotero.geo.util
import com.vividsolutions.jts.geom.Geometry

import org.geotools.data.simple.SimpleFeatureCollection

import org.geotools.data.Query

import com.marinotero.data.util.ShapefileUtil
import com.marinotero.geo.util._
import org.junit._
import Assert._
import scala.collection.JavaConversions._

class GeoprocessingUtilTest {

  val pathSeparator = System.getProperty("file.separator")
  val shpUtil = new ShapefileUtil()

  @Test
  def testBuffer() {
    val shp = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "Point.shp"
    val featureSource = shpUtil.readLayer(shp)
    assertNotNull(featureSource)
    val featureCollection = featureSource.getFeatures()
    val bufferedFeatureCollection = shpUtil.buffer(featureCollection, 0.1)
    assertNotNull(bufferedFeatureCollection)
    assertEquals(featureCollection.size, bufferedFeatureCollection.size)
    val path = System.getProperty("user.dir") + pathSeparator + "target" + pathSeparator + "buffer.shp"
    val schema = bufferedFeatureCollection.getSchema
    shpUtil.createLayer(path, schema, bufferedFeatureCollection)
  }

  @Test
  def mergeTest() {
    val point1 = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "Point.shp"
    val point2 = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "Point2.shp"
    val inputCollection = shpUtil.readLayer(point1).getFeatures
    val mergeCollection = shpUtil.readLayer(point2).getFeatures
    shpUtil.merge(inputCollection, mergeCollection)
  }

  @Test
  def unionTest() {
    val states = System.getProperty("user.dir") + pathSeparator + "src" + pathSeparator + "test" + pathSeparator + "resources" + pathSeparator + "state2000gen.shp"
    val inputFeatureCollection = shpUtil.readLayer(states).getFeatures
    val outputFeatureCollection = shpUtil.union(inputFeatureCollection)
    assertNotNull(outputFeatureCollection)
    shpUtil.createLayer(System.getProperty("user.dir") + pathSeparator + "target" + pathSeparator + "stateDiss.shp", outputFeatureCollection.getSchema, outputFeatureCollection)

  }

}