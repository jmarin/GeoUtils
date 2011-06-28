package com.marinotero.geo.util//import org.opengis.feature.type.AttributeDescriptor
import java.util.ArrayList

import com.vividsolutions.jts.geom.GeometryCollection
import org.geotools.geometry.jts.FactoryFinder

import org.geotools.referencing.CRS

import com.vividsolutions.jts.geom.Polygon

import com.vividsolutions.jts.geom.Geometry
import org.opengis.feature.simple.SimpleFeature
import org.geotools.feature.simple.SimpleFeatureBuilder
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import com.vividsolutions.jts.geom.MultiPolygon
import org.geotools.data.simple.SimpleFeatureCollection
import org.geotools.feature.FeatureCollections
import scala.collection.JavaConversions._

trait GeoprocessingUtil {

  def buffer(featureCollection: SimpleFeatureCollection, distance: Double): SimpleFeatureCollection = {
    println("Buffering to a distance of " + distance + "....")
    val bufferedFeatureCollection = FeatureCollections.newCollection()
    val schema = featureCollection.getSchema
    val attributeDescriptors = schema.getAttributeDescriptors
    val builder = new SimpleFeatureTypeBuilder
    builder.setName("buffer")
    builder.setCRS(schema.getCoordinateReferenceSystem)
    for (attributeDescriptor <- attributeDescriptors) {
      if (attributeDescriptor.getLocalName().asInstanceOf[String].equals("the_geom")) {
        builder.add("Location", classOf[Polygon])
      } else {
        builder.add(attributeDescriptor)
      }
    }
    val TYPE = builder.buildFeatureType
    val featureBuilder = new SimpleFeatureBuilder(TYPE).asInstanceOf[SimpleFeatureBuilder]
    val featureArray = featureCollection.iterator().toArray[SimpleFeature]
    featureArray.foreach(simpleFeature => {
      val inputGeometry = simpleFeature.getDefaultGeometry.asInstanceOf[Geometry]
      if (inputGeometry.getSRID() == 0)
        inputGeometry.setSRID(4326)
      val geometry = inputGeometry.buffer(distance)
      geometry.setSRID(CRS.lookupEpsgCode(schema.getCoordinateReferenceSystem, true).asInstanceOf[Int])
      featureBuilder.add(geometry)
      val values = simpleFeature.getAttributes
      for (i <- 1 to values.size - 1) {
        featureBuilder.add(values.get(i))
      }
      val bufferedFeature = featureBuilder.buildFeature(null)
      bufferedFeatureCollection.add(bufferedFeature)
    })
    bufferedFeatureCollection
  }
  def dissolve(featureCollection:SimpleFeatureCollection, attributeNames:List[String]) = {      }  
  def union(featureCollection: SimpleFeatureCollection): SimpleFeatureCollection = {
    val unionedFeatureCollection = FeatureCollections.newCollection()
    val factory = FactoryFinder.getGeometryFactory(null)
    val featureArray = featureCollection.iterator().toArray[SimpleFeature]
    val geometryList = new ArrayList[Geometry]
    featureArray.foreach(simpleFeature => {
      val geometry = simpleFeature.getDefaultGeometry.asInstanceOf[Geometry]
      geometryList.add(geometry) 
    })
    val builder = new SimpleFeatureTypeBuilder
    builder.setName("union")
    builder.add("Location", classOf[Polygon])
    val TYPE = builder.buildFeatureType
    val featureBuilder = new SimpleFeatureBuilder(TYPE).asInstanceOf[SimpleFeatureBuilder]
    println("Unioning " + geometryList.size + " geometries")
    val geometryCollection = factory.buildGeometry(geometryList)
    val unionedGeometries = geometryCollection.union()
    for (i <- 0 until unionedGeometries.getNumGeometries -1){
      featureBuilder.add(unionedGeometries.getGeometryN(i))
      val newFeature = featureBuilder.buildFeature(null)
      unionedFeatureCollection.add(newFeature)
    }
    unionedFeatureCollection  
  }      
  def merge(inputFeatureCollection: SimpleFeatureCollection, mergeFeatureCollection: SimpleFeatureCollection): SimpleFeatureCollection = {
    //Merge fields that are identical in definition to inputFeatureColleciton, discard the rest
    val mergedCollection = FeatureCollections.newCollection
    val inputSchema = inputFeatureCollection.getSchema
    val mergeSchema = mergeFeatureCollection.getSchema
    val inputAttributeDescriptors = inputSchema.getAttributeDescriptors
    val mergeAttributeDescriptors = mergeSchema.getAttributeDescriptors
    val attributeList = inputAttributeDescriptors.intersect(mergeAttributeDescriptors).toArray
    val attributeNamesList = Nil
    val builder = new SimpleFeatureTypeBuilder
    builder.setName("merge")
    builder.setCRS(inputFeatureCollection.getSchema().getCoordinateReferenceSystem)
    for (i <- 0 to attributeList.length - 1) {
      val attribute = attributeList(i)
      attribute.getLocalName :: attributeNamesList
      builder.add(attribute)
    }
    val TYPE = builder.buildFeatureType
    val featureBuilder = new SimpleFeatureBuilder(TYPE).asInstanceOf[SimpleFeatureBuilder]
    val inputFeatureArray = inputFeatureCollection.iterator().toArray
    for (i <- 0 to inputFeatureArray.length - 1) {
      val feature = inputFeatureArray(i)
      val geometry = feature.getDefaultGeometry().asInstanceOf[Geometry]
      featureBuilder.add(geometry)

    }
    /* inputFeatureCollection.iterator().foreach(simpleFeature => {
      val geometry = simpleFeature.getDefaultGeometry().asInstanceOf[Geometry]
      featureBuilder.add(geometry)
      val inputAttributes = simpleFeature.getAttributes
      inputAttributes.foreach(println)
      
    })*/

    mergedCollection
  }

}