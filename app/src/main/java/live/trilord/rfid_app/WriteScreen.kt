package live.trilord.rfid_app

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WriteScreen(context: Context, rfidManager: RFIDManager, navController: NavHostController) {
    var tagRead by remember { mutableStateOf("No tag read") }
    var writeSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Escribir en Tag RFID", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            rfidManager.enableTrigger { tag ->
                tagRead = tag
            }
        }) {
            Text("Leer Tag")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tag leído: $tagRead")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (tagRead != "No tag read") {
                writeSuccess = rfidManager.writeTag(tagRead, "123456789012")
            }
        }) {
            Text("Escribir en Tag")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = if (writeSuccess) "✅ Escrito con éxito" else "❌ No se ha escrito aún")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}
