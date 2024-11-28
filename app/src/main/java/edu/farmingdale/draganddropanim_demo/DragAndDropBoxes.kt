@file:OptIn(ExperimentalFoundationApi::class)

package edu.farmingdale.draganddropanim_demo

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {

    var rotationAngle by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isDropped by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            val boxCount = 4
            var dragBoxIndex by remember {
                mutableIntStateOf(0)
            }

            repeat(boxCount) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(10.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event
                                    .mimeTypes()
                                    .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = index
                                        isDropped = !isDropped
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.AnimatedVisibility(
                        visible = index == dragBoxIndex,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .background(Color.Red)
        ) {
            rotate(rotationAngle) {
                drawRect(
                    color = Color.Green,
                    topLeft = Offset(size.width / 2 - 50 + offsetX, size.height / 2 - 50 + offsetY),
                    size = androidx.compose.ui.geometry.Size(100f, 100f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = { rotationAngle += 45f }) {
                Text("Rotate")
            }

            Button(onClick = { offsetX += 20f }) {
                Text("Move Right")
            }
            Button(onClick = { offsetX -= 20f }) {
                Text("Move Left")
            }
            Button(onClick = { offsetY += 20f }) {
                Text("Move Down")
            }
            Button(onClick = { offsetY -= 20f }) {
                Text("Move Up")
            }

            Button(onClick = {
                rotationAngle = 0f
                offsetX = 0f
                offsetY = 0f
            }) {
                Text("Reset")
            }
        }

        AnimatedVisibility(
            visible = isDropped,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Dropped!",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
    }
}


