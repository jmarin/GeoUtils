package com.marinotero.projection.util
import org.geotools.geometry.jts.JTS

import com.vividsolutions.jts.geom.Geometry

import org.geotools.feature.simple.SimpleFeatureBuilder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.geotools.feature.FeatureCollections
import org.geotools.referencing.CRS
import org.geotools.data.simple.SimpleFeatureCollection
import scala.collection.JavaConversions._
import org.opengis.feature.simple.SimpleFeature

trait ProjectionUtil {

  def projectGeometry(inputGeometry: Geometry, inputSRID: Int, outputSRID: Int): Geometry = {
    val inputCRS = CRS.decode("EPSG:" + inputSRID, true)
    val outputCRS = CRS.decode("EPSG:" + outputSRID, true)
    val transform = CRS.findMathTransform(inputCRS, outputCRS)
    val geometry = JTS.transform(inputGeometry, transform)
    geometry
  }

  def projectFeatureCollection(inputFeatureCollection: SimpleFeatureCollection, inputSRID: Int, outputSRID: Int): SimpleFeatureCollection = {
    println("Projecting from " + inputSRID + " to " + outputSRID + "....")
    val projFeatureCollection = FeatureCollections.newCollection
    val TYPE = inputFeatureCollection.getSchema
    val csr = TYPE.getCoordinateReferenceSystem
    val inputCRS = CRS.decode("EPSG:" + inputSRID, true)
    var outputCRS = CRS.decode("EPSG:" + outputSRID, true)
    val transform = CRS.findMathTransform(inputCRS, outputCRS, true)
    val builder = new SimpleFeatureTypeBuilder
    builder.setName("projected")
    builder.setCRS(outputCRS)
    val featureBuilder = new SimpleFeatureBuilder(TYPE)
    val array = inputFeatureCollection.iterator().toArray[SimpleFeature]
    array.foreach(simpleFeature => {
      val geometry = simpleFeature.getDefaultGeometry().asInstanceOf[Geometry]
      val projGeometry = JTS.transform(geometry, transform)
      featureBuilder.add(projGeometry)
      val values = simpleFeature.getAttributes
      for (i <- 1 to values.size - 1) {
        featureBuilder.add(values.get(i))
      }
      val projFeature = featureBuilder.buildFeature(null)
      projFeatureCollection.add(projFeature)
    })
    projFeatureCollection
  }

  def getSRIDWGS84UTMCode(longitude: Double, latitude: Double): Int = {
    var base = 0
    if (latitude >= 0) {
      base = 32600
    } else {
      base = 32700
    }
    val code = base + getWGS84UTMZoneNumber(longitude, latitude)
    return code
  }

  def getEPSGWGS84UTMCode(longitude: Double, latitude: Double): String = {
    var base = 0;
    if (latitude >= 0) {
      base = 32600
    } else {
      base = 32700
    }
    val code = base + getWGS84UTMZoneNumber(longitude, latitude);
    return "EPSG:" + code;
  }

  def getWGS84UTMZoneNumber(longitude: Double, latitude: Double): Int = {
    if (longitude == 180) {
      60
    } else if (latitude >= 56.0f && latitude < 64.0f && longitude >= 3.0f
      && longitude < 12.0f) {
      // Norway's exception zone
      32
    } else if (latitude >= 72.0f && latitude < 84.0f) {
      // Svalbard Exceptions
      if (longitude >= 0.0f && longitude < 9.0f) {
        31
      } else if (longitude >= 9.0f && longitude < 21.0f) {
        33
      } else if (longitude >= 21.0f && longitude < 33.0f) {
        35
      } else if (longitude >= 33.0f && longitude < 42.0f) {
        37
      }
      0
    } else {
      val code = (Math.floor((longitude + 180) / 6) + 1).asInstanceOf[Int];
      code
    }
  }

}