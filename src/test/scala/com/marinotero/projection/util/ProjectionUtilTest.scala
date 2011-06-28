package com.marinotero.projection.util
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.Coordinate
import org.junit._
import Assert._
import com.marinotero.data.util.ShapefileUtil

class ProjectionUtilTest {

  val projectionUtil = new ShapefileUtil
  
  @Test
  def testProjectPoint() {
    val geometryFactory = new GeometryFactory
    val coordinate = new Coordinate
    coordinate.x = 412801.1200222892
    coordinate.y = 4261767.158486189
    val inputPoint = geometryFactory.createPoint(coordinate)
    val outputPoint = projectionUtil.projectGeometry(inputPoint, 4326, 3785)
    assertNotNull(outputPoint)
    println("Projected to POINT(-77.5 38.0) in WGS84 from "
      + outputPoint.toText() + " in Web Mercator")
  }

  @Test
  def testGetEPSGWGS84UTMCode() {
    val longitude = -77.5;
    val latitude = 38.0;
    val utmZone = projectionUtil.getWGS84UTMZoneNumber(longitude, latitude);
    val epsg = projectionUtil.getEPSGWGS84UTMCode(longitude, latitude);
    assertEquals(utmZone, 18);
    assertEquals(epsg, "EPSG:32618");
  }

}