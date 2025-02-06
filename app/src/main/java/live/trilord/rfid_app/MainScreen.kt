package live.trilord.rfid_app


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MainScreen(context: Context, rfidManager: RFIDManager, navController: NavHostController) {
    var tagList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isConnected by remember { mutableStateOf(false) }
    var tagRead by remember { mutableStateOf("No Tags Read") }

    LaunchedEffect(Unit) { // Solo inicializa una vez
        isConnected = rfidManager.initializeReader()
        if (isConnected) {
            Toast.makeText(context, "Reader Connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to Connect", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "RFID Reader", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            rfidManager.disconnectReader()
            isConnected = false
            Toast.makeText(context, "Reader Disconnected", Toast.LENGTH_SHORT).show()
        }) {
            Text("Disconnect Reader")
        }

        Button(onClick = {
            if (isConnected) {
                rfidManager.enableTrigger { tag ->
                    tagList = tagList + tag // Agrega tags en tiempo real
                }
                Toast.makeText(context, "Iniciando lectura", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Leer")
        }

        Button(onClick = {
            rfidManager.disableTrigger()
            Toast.makeText(context, "Stop Inventory", Toast.LENGTH_SHORT).show()
        }) {
            Text("Dejar de leer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicText(text = "Status: ${if (isConnected) "Connected" else "Disconnected"}")

        LazyColumn {
            items(tagList) { tag ->
                Text(text = tag)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir a la pantalla de escritura
        Button(onClick = { navController.navigate("write") }) {
            Text("Ir a Escribir Tag")
        }
    }
}
