package com.android.myweatherapp



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.myweatherapp.api.NetworkResponse
import com.android.myweatherapp.api.WeatherModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel){
    var city by remember {
        mutableStateOf("")
    }
    val weatherResult = viewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly){
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.green),
                    unfocusedLabelColor = colorResource(id = R.color.darkGreen)
                ),
                singleLine = true,
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                label = { Text(text = "Search for any location", color = colorResource(id = R.color.green))})

            Button(modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.darkGreen),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                viewModel.getData(city)
                keyboardController?.hide() }
            ) {
                Text(text = "Search")
            }
        }
        when(val result = weatherResult.value){
             is NetworkResponse.Error ->  {
                 Text(text = result.message)
             }
             NetworkResponse.Loading  ->  {
                 CircularProgressIndicator(
                     modifier = Modifier
                         .padding(2.dp)
                         .align(Alignment.CenterHorizontally),
                     color = Color.Blue,
                 )
             }
             is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
             }
             null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
       Row (
           modifier = Modifier.fillMaxWidth(),
           horizontalArrangement = Arrangement.Start,
           verticalAlignment = Alignment.Bottom
       ){
           Icon(
               imageVector = Icons.Default.LocationOn,
               contentDescription = "Location Icon",
               tint = colorResource(id = R.color.darkGreen),
               modifier = Modifier.size(40.dp))
           Text(text = data.location.name,
               fontSize = 30.sp,
               color = colorResource(id = R.color.darkGreen))
           Spacer(modifier = Modifier.width(8.dp))
           Text(text = data.location.country,
               fontSize = 18.sp,
               color = colorResource(id = R.color.green))
       }
        Spacer(modifier = Modifier.padding(16.dp))
        Text(text = "${data.current.temp_c} ° c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.darkGreen)
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}"
            , contentDescription = "Condition icon")

        Text(text = "${data.current.condition.text}, Feels Like ${data.current.feelslike_c}°",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.darkGreen)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card (
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.background))
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                         horizontalArrangement = Arrangement.Absolute.SpaceAround
                ){
                   WeatherKeyVal("Humidity","${data.current.humidity}%")
                   WeatherKeyVal("Wind Speed","${data.current.wind_kph} kph")
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                         horizontalArrangement = Arrangement.Absolute.SpaceAround
                ){
                    WeatherKeyVal("Visibility","${data.current.vis_km} km")
                    WeatherKeyVal("Dew Point","${data.current.dewpoint_c}°")
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ){
                    WeatherKeyVal("Precipitation","${data.current.precip_mm} mm")
                    WeatherKeyVal("UV Index",data.current.uv)
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key:String,value:String){
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.darkGreen))
        Text(text = key, fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.green) )
    }
}