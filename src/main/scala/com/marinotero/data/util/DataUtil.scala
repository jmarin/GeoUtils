package com.marinotero.data.util
import org.geotools.data.simple.SimpleFeatureSource
import org.opengis.feature.simple.SimpleFeature; import org.opengis.feature.simple.SimpleFeatureType
import org.geotools.data.simple.SimpleFeatureCollection
import scala.collection.JavaConversions._

trait DataUtil {

  def readLayer(path: String): SimpleFeatureSource

  def createLayer(path: String, schema: SimpleFeatureType, featureCollection: SimpleFeatureCollection)
 
 
}