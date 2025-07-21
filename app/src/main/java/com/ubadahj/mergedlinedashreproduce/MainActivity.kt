package com.ubadahj.mergedlinedashreproduce

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ubadahj.mergedlinedashreproduce.ui.theme.AppTheme
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapLibreMapOptions
import org.maplibre.android.maps.MapView
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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

const val MAP_STYLE_URL = "https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        MapLibre.getInstance(this)

        val counter = mutableIntStateOf(0)
        val timer = flow {
            var duration = 0.seconds
            while (currentCoroutineContext().isActive) {
                delay(1.seconds)
                duration += 1.seconds
                emit(duration)
            }
        }

        setContent {
            Layout(
                background = {
                    MapView(
                        modifier = Modifier.fillMaxSize(),
                        url = "$MAP_STYLE_URL?q=${counter.intValue}"
                    )
                },
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Card {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.renderer),
                                    style = MaterialTheme.typography.titleLargeEmphasized,
                                    color = MaterialTheme.colorScheme.primary,
                                )

                                OutlinedCard {
                                    val time by timer.collectAsState(initial = 0.seconds)

                                    Text(
                                        modifier = Modifier.padding(12.dp),
                                        text = time.formatted(),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        RefreshButton(counter = counter.intValue) {
                            counter.intValue += 1
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Layout(
    background: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    AppTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { inner ->
            background()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(12.dp),
                content = content,
            )
        }
    }
}

@Composable
private fun MapView(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx -> MapView(ctx, mapOpts(ctx)) },
        update = { mv ->
            mv.getMapAsync { map ->
                map.setStyle(url) { configure(map, it) }
            }
        }
    )
}

@Composable
private fun RefreshButton(counter: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    BadgedBox(
        modifier = modifier,
        badge = { Badge { Text("$counter") } }
    ) {
        FloatingActionButton(onClick) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh",
            )
        }
    }
}

private fun mapOpts(ctx: Context) = MapLibreMapOptions
    .createFromAttributes(ctx)
    .compassEnabled(false)

private fun Duration.formatted() = if (inWholeHours > 0) {
    "%02d:%02d:%02d".format(
        inWholeHours,
        inWholeMinutes % 60,
        inWholeSeconds % 60
    )
} else {
    "%02d:%02d".format(
        inWholeMinutes,
        inWholeSeconds % 60
    )
}