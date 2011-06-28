package com.marinotero.geo.util
import com.marinotero.data.util.ShapefileUtil
import org.junit._
import Assert._
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Coordinate
import org.geotools.data._
import com.vividsolutions.jts.io.WKTReader
import org.geotools.feature.simple.SimpleFeatureBuilder

class GeoUtilTest {

  @Test
  def testIsValidPoint() {
    val geometryFactory = new GeometryFactory()
    val point = geometryFactory.createPoint(new Coordinate(-77.5, 38.0))
    point.setSRID(4326)
    assertTrue(getGeoUtil.isValidGeometry(point))
  }

 @Test
  def testValidListOfFeatures() {
    val TYPE = DataUtilities.createType("location", "geom:Polygon,name:String")
    val wktReader = new WKTReader()
    val geometryFactory = new GeometryFactory()
    val validFeature1 = SimpleFeatureBuilder.build(TYPE, Array(wktReader.read("POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))").asInstanceOf[Object], "name1"), null)
    val validFeature2 = SimpleFeatureBuilder.build(TYPE, Array(wktReader.read("POLYGON((1 1, 2 1, 2 2, 1 2, 1 1))").asInstanceOf[Object], "name1"), null)
    val invalidFeature1 = SimpleFeatureBuilder.build(TYPE, Array(wktReader.read("POLYGON((0 0, 1 0, 0 1, 1 1, 0 0))").asInstanceOf[Object], "name1"), null)
    val invalidFeature2 = SimpleFeatureBuilder.build(TYPE, Array(wktReader.read("POLYGON((1 1, 2 1, 1 2, 2 2, 1 1))").asInstanceOf[Object], "name1"), null)
    val invalidFeature3 = SimpleFeatureBuilder.build(TYPE, Array(wktReader.read("POLYGON((0 0, 2 0, 0 2, 2 2, 0 0))").asInstanceOf[Object], "name1"), null)
    val list = List(validFeature1, validFeature2,invalidFeature1, invalidFeature2, invalidFeature3)
    val validList = getGeoUtil.getValidFeaturesList(list)
    val invalidList = getGeoUtil.getInvalidFeaturesList(list)
    assertEquals(validList.size,2)
    assertEquals(invalidList.size,3)
  }
  
  @Test
  def testReadShapefile() {
	  val shpUtil = new ShapefileUtil()
	  val shp = System.getProperty("user.dir") + "/src/test/resources/state2000gen.shp"
	  val featureSource = shpUtil.readLayer(shp)
	  assertNotNull(shp)
	  
  }

  def getGeoUtil() = {
    new GeoUtil()
  }

}