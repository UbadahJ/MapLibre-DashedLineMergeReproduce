package com.ubadahj.mergedlinedashreproduce

import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.lineCap
import org.maplibre.android.style.layers.PropertyFactory.lineColor
import org.maplibre.android.style.layers.PropertyFactory.lineDasharray
import org.maplibre.android.style.layers.PropertyFactory.lineJoin
import org.maplibre.android.style.layers.PropertyFactory.lineWidth
import org.maplibre.android.style.layers.PropertyFactory.visibility
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point

const val SOURCE_ID = "source"
const val LAYER_ID = "layer"
const val CENTRE_LAT = 39.749
const val CENTRE_LONG = -105.005
const val CENTRE_ZOOM = 10.0
const val TOTAL_POINTS = 10000
const val INCREMENT = 0.0001


fun configure(map: MapLibreMap, style: Style) {
    val source = style.getSource(SOURCE_ID) ?: GeoJsonSource(SOURCE_ID).apply {
        setGeoJson(
            LineString.fromLngLats(List(TOTAL_POINTS) {
                Point.fromLngLat(CENTRE_LONG + it * INCREMENT, CENTRE_LAT + it * INCREMENT)
            })
        )

        style.addSource(this)
    }

    style.getLayer(LAYER_ID) ?: LineLayer(LAYER_ID, source.id).apply {
        setProperties(
            lineCap(Property.LINE_CAP_ROUND),
            lineJoin(Property.LINE_JOIN_ROUND),
            visibility(Property.VISIBLE),
            lineWidth(3f),
            lineColor("#FF0000"),
            lineDasharray(arrayOf(1f, 1.50f))
        )

        style.addLayer(this)
    }

    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(CENTRE_LAT, CENTRE_LONG), CENTRE_ZOOM))
}
