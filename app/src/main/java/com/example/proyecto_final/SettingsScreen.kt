package com.example.proyecto_final

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Settings(navController: NavController){
    var showTonos by rememberSaveable { mutableStateOf(false) }
    var tono by rememberSaveable { mutableStateOf("EL MAMBO") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .clickable {
                    navController.navigate(AppScreens.MainScreen.route + "/${tono}")
                },
            tint = Color.White
        )
        Spacer(Modifier.height(22.dp))
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Ajustes",
                color = Color.White,
                fontSize = TextUnit(40f, TextUnitType.Sp),
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(Modifier.height(20.dp))
        Divider()
        Spacer(Modifier.height(15.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (showTonos == false) {
                        showTonos = true
                    } else {
                        showTonos = false
                    }
                }
        ){
            if(showTonos == true){
                AlertDialog(
                    onDismissRequest = { showTonos = false },
                    title = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Tono de alarma",
                                fontSize = TextUnit(30f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(80.dp)
                                    .clickable {
                                        tono = "EL MAMBO"
                                        Toast.makeText(context,"Tono seleccionado: " + tono, Toast.LENGTH_SHORT).show()
                                        showTonos = false
                                    }
                            ){
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(60.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black
                                    )
                                ) {
                                    Text(
                                        text = "- EL MAMBO",
                                        fontSize = TextUnit(30f, TextUnitType.Sp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Divider()
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(80.dp)
                                    .clickable {
                                        tono = "FNAF"
                                        Toast.makeText(context,"Tono seleccionado: " + tono, Toast.LENGTH_SHORT).show()
                                        showTonos = false
                                    }
                            ){
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(60.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black
                                    )
                                ) {
                                    Text(
                                        text = "- FNAF",
                                        fontSize = TextUnit(30f, TextUnitType.Sp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Divider()
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(80.dp)
                                    .clickable {
                                        tono = "DETRAS DEL HUMO"
                                        Toast.makeText(context,"Tono seleccionado: " + tono, Toast.LENGTH_SHORT).show()
                                        showTonos = false
                                    }
                            ){
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(60.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Black
                                    )
                                ){
                                    Text(
                                        text = "- DETRAS DEL HUMO",
                                        fontSize = TextUnit(30f, TextUnitType.Sp),
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = { /*TODO*/ },
                    containerColor = Color.DarkGray
                )
            }
            Text(
                text = "Tono de alarma",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(15f, TextUnitType.Sp)
            )
            Spacer(Modifier.width(25.dp))
            Text(
                text = tono,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp),
                tint = Color.White
            )
        }
    }
}