package com.vandenbreemen.videogameusr.view.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vandenbreemen.videogameusr.view.ButtonColors
import com.vandenbreemen.videogameusr.view.Dimensions
import com.vandenbreemen.videogameusr.view.VideoGameUserTheme

@Composable
fun ConfirmingButton(
    text: String,
    areYouSureMsg: String = "Are you sure?",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    val isConfirming = remember { mutableStateOf(false) }

    Card(elevation= Dimensions.elevation, shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(horizontal = Dimensions.borderPadding)) {
        Row(modifier = Modifier.padding(Dimensions.borderPadding)) {
            if(!isConfirming.value) {
                Button(
                    onClick = {
                        isConfirming.value = true
                    }, modifier=Modifier.height((Dimensions.padding.value*2.5).dp)
                ) {
                    Text(text, style = MaterialTheme.typography.button)
                }
            }
            else {
                Column(modifier=Modifier.background(MaterialTheme.colors.secondary).padding(horizontal = Dimensions.padding, vertical = 2.dp)) {
                    Text(areYouSureMsg, style = MaterialTheme.typography.button)
                    Row() {
                        Button(
                            onClick = {
                                isConfirming.value = false
                                onCancel()
                            }, modifier=Modifier.width((Dimensions.padding.value * 4.5).dp).height((Dimensions.padding.value*2.5).dp)
                        ) {
                            Text("No", style = MaterialTheme.typography.button)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                isConfirming.value = false
                                onConfirm()
                            }, modifier=Modifier.width((Dimensions.padding.value * 5).dp).height((Dimensions.padding.value*2.5).dp)
                        ) {
                            Text("Yes", style = MaterialTheme.typography.button.copy(color = ButtonColors.irreversableChange))
                        }
                    }
                }
            }
        }

    }
}

@Composable
@Preview
fun ConfirmingButtonPreview() {
    VideoGameUserTheme {
        ConfirmingButton("Test", "Are you sure", { println("Confirmed") }, { println("Cancelled") })
    }
}

fun main() {
    confirmButtonTest()

}

fun confirmButtonTest() = application {
    Window(
        onCloseRequest = {  },
        visible = true,
        title = "Confirming Button Test",
    ) {
        VideoGameUserTheme {
            ConfirmingButton("Test", "You sure", { println("Confirmed") }, { println("Cancelled") })
        }
    }
}