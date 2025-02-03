package live.trilord.rfid_app


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(context: Context, rfidManager: RFIDManager) {
    var isConnected by remember { mutableStateOf(false) }
    var tagRead by remember { mutableStateOf("No Tags Read") }

    isConnected = rfidManager.initializeReader()
    if (isConnected) {
        Toast.makeText(context, "Reader Connected", Toast.LENGTH_SHORT).show()

    } else {
        Toast.makeText(context, "Failed to Connect", Toast.LENGTH_SHORT).show()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "RFID Reader", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (isConnected) {
                rfidManager.startInventory { tag ->
                    tagRead = tag
                }
            }
        }) {
            Text("Start Inventory")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            rfidManager.disconnectReader()
            isConnected = false
            Toast.makeText(context, "Reader Disconnected", Toast.LENGTH_SHORT).show()
        }) {
            Text("Disconnect Reader")
        }

        Spacer(modifier = Modifier.height(16.dp))

        BasicText(text = "Status: ${if (isConnected) "Connected" else "Disconnected"}")
        BasicText(text = "Tag Data: $tagRead")
    }
}
