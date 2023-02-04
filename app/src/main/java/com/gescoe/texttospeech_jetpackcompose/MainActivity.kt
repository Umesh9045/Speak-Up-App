package com.gescoe.texttospeech_jetpackcompose

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gescoe.texttospeech_jetpackcompose.ui.theme.TextToSpeechJetpackComposeTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    private  var  tts:TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TextToSpeechJetpackComposeTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scaffoldState= rememberScaffoldState()
                    var textFieldState by remember{
                        mutableStateOf("")
                    }
                    var isBtnEnabled by remember{
                        mutableStateOf(true)
                    }
                    val scope= rememberCoroutineScope()
                    var pitch by remember { mutableStateOf(1f) }
                    var speechRate by remember { mutableStateOf(1f) }
                    Scaffold(
                        modifier=Modifier.fillMaxSize(),
                        scaffoldState=scaffoldState
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 30.dp)
                        ) {
                            TextField(
                                value = textFieldState,
                                label={
                                    Text("Enter some text here")
                                },
                                onValueChange = {
                                    textFieldState=it
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(35.dp))
                            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically){
                                Text("Pitch")
                                Slider(
                                    value = pitch/3,
                                    onValueChange = { pitch= it*3 },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                                Text("Speed")
                                Slider(
                                    value = speechRate /3,
                                    onValueChange = { speechRate = it * 3 },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Button(onClick={
                                if(isBtnEnabled)
                                    isBtnEnabled=false
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(textFieldState)
                                }
                                tts= TextToSpeech(
                                    applicationContext
                                ) {
                                    if (it == TextToSpeech.SUCCESS) {
                                        tts?.let { txtToSpeech ->
                                            txtToSpeech.language = Locale.US
                                            txtToSpeech.setPitch(pitch)
                                            txtToSpeech.setSpeechRate(speechRate)
                                            txtToSpeech.speak(
                                                textFieldState,
                                                TextToSpeech.QUEUE_ADD,
                                                null,
                                                null
                                            )
                                        }
                                    }
                                }
                                isBtnEnabled=true
                            },
                                enabled=isBtnEnabled,
                                modifier=Modifier.height(50.dp).width(120.dp)
                            ){
                                Text("Speak", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
