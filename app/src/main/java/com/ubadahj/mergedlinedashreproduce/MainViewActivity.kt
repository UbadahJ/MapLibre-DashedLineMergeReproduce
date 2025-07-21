package com.ubadahj.mergedlinedashreproduce

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
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

class MainViewActivity : ComponentActivity() {
    private lateinit var mapView: MapView
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this)
        setContentView(R.layout.main_activity)
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            it.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        CENTRE_LAT,
                        CENTRE_LONG
                    ), CENTRE_ZOOM
                )
            )

            configure(it)
            val button = findViewById<Button>(R.id.refresh_button)
            button.setOnClickListener { _ ->
                configure(it)
            }
        }
    }

    private fun configure(map: MapLibreMap) {
        map.setStyle("https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json?q=${counter++}") { style ->
            val source = style.getSource(SOURCE_ID) ?: GeoJsonSource(SOURCE_ID).apply {
                setGeoJson(
                    LineString.fromLngLats(List(TOTAL_POINTS) {
                        Point.fromLngLat(CENTRE_LONG + it * INCREMENT, CENTRE_LAT + it * INCREMENT)
                    })
                )

                Log.d("MainViewActivity", "Adding source with id: $id")
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

                Log.d("MainViewActivity", "Adding layer with id: $id")
                style.addLayer(this)
            }

            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        CENTRE_LAT,
                        CENTRE_LONG
                    ), CENTRE_ZOOM
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
