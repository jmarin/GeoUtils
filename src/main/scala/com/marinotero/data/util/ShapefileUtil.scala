package com.marinotero.data.util
import org.geotools.data.FeatureStore

import java.net.URL

import org.geotools.data.Transaction
import org.geotools.data.simple.SimpleFeatureStore
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.geotools.data.shapefile.ShapefileDataStore
import java.util.HashMap
import org.geotools.data.shapefile.ShapefileDataStoreFactory
import org.geotools.feature.FeatureCollection
import org.geotools.data.DefaultTransaction
import com.marinotero.geo.util._
import org.geotools.data.simple.SimpleFeatureCollection
import org.opengis.feature.simple.SimpleFeature
import org.opengis.feature.simple.SimpleFeatureType
import org.geotools.data.FileDataStoreFinder
import java.io.File
import org.geotools.data.simple.SimpleFeatureSource
import scala.collection.JavaConversions._
import com.marinotero.projection.util.ProjectionUtil
class ShapefileUtil extends DataUtil with GeoprocessingUtil with TopologyUtil with ProjectionUtil {

  override def readLayer(path: String): SimpleFeatureSource = {
    val file: File = new File(path)
    if (file != null) {
      val store = FileDataStoreFinder.getDataStore(file)
      val featureSource = store.getFeatureSource().asInstanceOf[SimpleFeatureSource]
      featureSource
    } else {
      null
    }
  }

  override def createLayer(path: String, schema: SimpleFeatureType, featureCollection: SimpleFeatureCollection) = {
    val file = new File(path); val url = file.toURI().toURL()
    val shpDataStore = new ShapefileDataStore(url)
    shpDataStore.createSchema(schema)
    val name = shpDataStore.getTypeNames()(0)
    val featureStore = shpDataStore.getFeatureSource(name).asInstanceOf[SimpleFeatureStore]
    val transaction = featureStore.getTransaction()
    try {
      featureStore.addFeatures(featureCollection)
    } finally {
      transaction.commit()
      transaction.close()
    }
  }
}