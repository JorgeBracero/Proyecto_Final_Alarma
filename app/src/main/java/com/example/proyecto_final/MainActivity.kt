package com.example.proyecto_final

import android.app.TimePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_final.ui.theme.Proyecto_FinalTheme
import java.lang.Math.abs
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proyecto_FinalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = AppScreens.MainScreen.route + "/{tono}"){
                        composable(AppScreens.MainScreen.route  + "/{tono}",
                            arguments = listOf(
                                navArgument("tono",{ NavType.StringType}))
                            ){
                            backStackEntry ->
                            val tono = backStackEntry.arguments?.getString("tono") ?: "EL MAMBO"
                            Screen(navController,tono)
                        }
                        composable(AppScreens.SettingsScreen.route){
                            Settings(navController)
                        }
                    }
                }
            }
        }
    }
}

private fun elegirCancion(tono: String): Int{
    var idCancion = 0
    when (tono) {
        "EL MAMBO" -> {
            idCancion = R.raw.mambo
        }

        "FNAF" -> {
            idCancion = R.raw.fnaf
        }

        "DETRAS DEL HUMO" -> {
            idCancion = R.raw.humo
        }
    }
    return idCancion
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(navController: NavController, tono: String){
    var alarmas: List<Alarma> by rememberSaveable { mutableStateOf(List(1){ Alarma("11:50",true) }) }
    val horaPartes = LocalTime.now().toString().split(":")
    val horaSistema = horaPartes[0] + ":" + horaPartes[1]
    /*var horaEdit by rememberSaveable { mutableStateOf("")}
    var alarmaSeleccionada = Alarma("",false)
    var showAlarma by rememberSaveable { mutableStateOf(false) }*/
    var reproductor by remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current
    Log.i("horaActual",horaSistema)
    Log.i("tonoSeleccionado", tono)

    //Cancion seleccionada por el usuario
    val idCancion = elegirCancion(tono)
    reproductor = MediaPlayer.create(context, idCancion)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            var idAlarma = R.drawable.alarmaon
            var imgSelected by rememberSaveable { mutableStateOf(false) }
            if(imgSelected){
                idAlarma = R.drawable.alarmaoff
            }
            Image(
                painter = painterResource(id = idAlarma),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        imgSelected = !imgSelected
                    }
            )
            Spacer(Modifier.width(160.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .clickable {
                        navController.navigate(AppScreens.SettingsScreen.route)
                    },
                    tint = Color.White
                )
        }
        Spacer(modifier = Modifier.height(20.dp))
        var showDialog by rememberSaveable { mutableStateOf(false) }
        //RecyclerView
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(alarmas) { alarma ->
                    showDialog = TarjetaAlarma(alarma)
                    /*if(showDialog){
                        alarmaSeleccionada.hora = alarma.hora
                        alarmaSeleccionada.activa = alarma.activa
                    }*/
                    Log.i("horaAlarma",alarma.hora)
                    Log.i("estadoAlarma","" + alarma.activa)
                }
            }


            var showAddDialog by rememberSaveable {mutableStateOf(false)}
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(15.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .size(70.dp),
                        onClick = {
                            showAddDialog = true
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .size(35.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            var time by rememberSaveable { mutableStateOf("") }
            if(showAddDialog){
                val placeholder = "HH:MM"
                AlertDialog(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Añadir alarma",
                                fontSize = TextUnit(30f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    text = {
                        TextField(
                            value = time,
                            onValueChange = {time = it},
                            placeholder = {
                                Text(
                                    text = placeholder,
                                    color = Color.White,
                                    fontSize = TextUnit(18f,TextUnitType.Sp)
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Gray,
                                textColor = Color.White
                            )
                        )
                    },
                    onDismissRequest = { showAddDialog = false },
                    confirmButton = {
                        Spacer(Modifier.width(20.dp))
                        Button(
                            onClick = {
                                val nuevaAlarma = Alarma(time,false)
                                alarmas = alarmas + nuevaAlarma
                                Toast.makeText(context,"Alarma añadida",Toast.LENGTH_SHORT).show()
                                showAddDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Aceptar",
                                fontSize = TextUnit(20f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showAddDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Cancelar",
                                fontSize = TextUnit(20f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    containerColor = Color.DarkGray
                )
            }

        }

        //Se queda pillado el dialogo al editar la alarma
        /*if(showDialog) {
            AlertDialog(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Editar alarma",
                            fontSize = TextUnit(30f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    horaEdit = InfoTarjeta(alarma = alarmaSeleccionada)
                },
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            alarmaSeleccionada.hora = horaEdit
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Listo",
                            fontSize = TextUnit(20f, TextUnitType.Sp),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                containerColor = Color.DarkGray
            )
        }*/

    }
    sonidoAlarma(alarmas, reproductor, horaSistema)
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTarjeta(alarma: Alarma): String {
    var timeSelected by rememberSaveable { mutableStateOf("") }
    var showTime by rememberSaveable {mutableStateOf(false)}
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )
    ){
        Column(
            modifier = Modifier
                .padding(20.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = alarma.hora,
                    color = Color.White,
                    fontSize = TextUnit(35f,TextUnitType.Sp),
                    fontFamily = FontFamily.Monospace
                )
                var estado by rememberSaveable {
                    mutableStateOf(alarma.activa)
                }
                Switch(
                    checked = estado,
                    onCheckedChange = {
                        estado = it
                        alarma.activa = it
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color.White,
                        checkedThumbColor = Color.White,
                        uncheckedBorderColor = Color.White,
                        checkedBorderColor = Color.White,
                        uncheckedTrackColor = Color.Black,
                        checkedTrackColor = Color.Blue
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Alarma diaria",
                    color = Color.White,
                    fontSize = TextUnit(15f, TextUnitType.Sp),
                )
            }
        }
        TextField(
            value = timeSelected,
            onValueChange = {
                timeSelected = it
            },
            placeholder = {
                Text(
                    text = "HH:MM",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(15f, TextUnitType.Sp)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.DarkGray,
                textColor = Color.White
            )
        )
    }
    return timeSelected
}*/


@Composable
fun sonidoAlarma(alarmas: List<Alarma>, reproductor: MediaPlayer?, horaSistema: String){
    val context = LocalContext.current
    var showAlarma by rememberSaveable { mutableStateOf(false) }
    for (alarma in alarmas) {
        showAlarma = false
        //Alarma activa
        if (alarma.hora.equals(horaSistema) && alarma.activa) {
            Log.i("ringring", "alarma activa")
            showAlarma = true
        }
    }

    if (showAlarma) {
        reproductor?.start()
        Log.i("he entraoooo", "entre")
        AlertDialog(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Alarma",
                        fontSize = TextUnit(30f, TextUnitType.Sp),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.alarmaring),
                        contentDescription = null
                    )
                }
            },
            onDismissRequest = {
                Toast.makeText(context,"Alarma parando...",Toast.LENGTH_SHORT).show()
                reproductor?.stop()
                reproductor?.release()
                showAlarma = false
            },
            confirmButton = {},
            dismissButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        Toast.makeText(context,"Alarma parando...",Toast.LENGTH_SHORT).show()
                        reproductor?.stop()
                        reproductor?.release()
                        showAlarma = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Detener",
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        textAlign = TextAlign.Center
                    )
                }
            },
            containerColor = Color.DarkGray
        )
    }

}

@Composable
fun TarjetaAlarma(alarma: Alarma): Boolean {
    val context = LocalContext.current
    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                Toast.makeText(context,"Alarma seleccionada",Toast.LENGTH_SHORT).show()
                showDialog = true
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ){
        Column(
            modifier = Modifier
                .padding(20.dp)
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = alarma.hora,
                    color = Color.White,
                    fontSize = TextUnit(40f,TextUnitType.Sp)
                )
                var estado by rememberSaveable {
                    mutableStateOf(alarma.activa)
                }
                Switch(
                    checked = estado,
                    onCheckedChange = {
                        estado = it
                        alarma.activa = it
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color.White,
                        checkedThumbColor = Color.White,
                        uncheckedBorderColor = Color.White,
                        checkedBorderColor = Color.White,
                        uncheckedTrackColor = Color.Black,
                        checkedTrackColor = Color.Blue
                    )
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Alarma diaria",
                    color = Color.White,
                    fontSize = TextUnit(15f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    return showDialog
}

/*
@Composable
fun DialogEditAlarma(alarma: Alarma){
    var horaEdit by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }
    AlertDialog(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Editar alarma",
                    fontSize = TextUnit(30f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            horaEdit = InfoTarjeta(alarma)
        },
        onDismissRequest = {
            showDialog = false
        },
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    alarma.hora = horaEdit
                    showDialog = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Listo",
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = Color.DarkGray
    )

}*/


//Clase Alarma
data class Alarma(var hora: String, var activa: Boolean)