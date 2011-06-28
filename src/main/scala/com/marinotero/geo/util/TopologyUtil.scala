package com.marinotero.geo.util
import com.marinotero.util.FileUtil

import java.io.File

import org.geotools.feature.FeatureCollections

import com.vividsolutions.jts.operation.valid.IsValidOp

import com.vividsolutions.jts.operation.valid.TopologyValidationError

import com.vividsolutions.jts.geom.Geometry
import org.geotools.data.simple.SimpleFeatureSource
import org.opengis.feature.simple.SimpleFeature
import org.geotools.data.simple.SimpleFeatureCollection
import scala.collection.JavaConversions._

trait TopologyUtil {

  def isValidGeometry(geometry: Geometry) = {
    geometry.isValid()
  }

  def getTopologyError(geometry: Geometry): TopologyValidationError = {
    val validOp = new IsValidOp(geometry)
    val topologyValidationError = validOp.getValidationError
    topologyValidationError
  }

  def getValidFeaturesArray(featureCollection: SimpleFeatureCollection): Array[SimpleFeature] = {
    val array = featureCollection.iterator().toArray[SimpleFeature]
    array.filter(simpleFeature => simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
  }

  def getInvalidFeaturesArray(featureCollection: SimpleFeatureCollection): Array[SimpleFeature] = {
    val array = featureCollection.iterator().toArray[SimpleFeature]
    array.filter(simpleFeature => !simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
  }

  def getValidFeaturesList(list: List[SimpleFeature]): List[SimpleFeature] = {
    list.filter(simpleFeature => simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
  }

  def getInvalidFeaturesList(list: List[SimpleFeature]): List[SimpleFeature] = {
    list.filter(simpleFeature => !simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
  }

  def fixGeometryCollection(featureCollection: SimpleFeatureCollection, logFileName: String): SimpleFeatureCollection = {
    println("Fixing geometries....")
    val fileUtil = new FileUtil
    val fixedGeometryCollection = FeatureCollections.newCollection()
    val array = featureCollection.iterator().toArray[SimpleFeature]
    val validCollection = array.filter(simpleFeature => simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
    validCollection.foreach(simpleFeature => {
      fixedGeometryCollection.add(simpleFeature)
    })
    val invalidCollection = array.filter(simpleFeature => !simpleFeature.getDefaultGeometry.asInstanceOf[Geometry].isValid)
    invalidCollection.foreach(simpleFeature => {
      fileUtil.appendToFile(logFileName, simpleFeature.getID() + ": " + getTopologyError(simpleFeature.getDefaultGeometry().asInstanceOf[Geometry]))	
      println("Topology error in " + simpleFeature.getID() + ": " + getTopologyError(simpleFeature.getDefaultGeometry().asInstanceOf[Geometry]))
      val geometry = simpleFeature.getDefaultGeometry.asInstanceOf[Geometry]
      val newGeometry = geometry.buffer(0.0)
      simpleFeature.setDefaultGeometry(newGeometry)
      fixedGeometryCollection.add(simpleFeature)
    })
    fixedGeometryCollection
  }

}