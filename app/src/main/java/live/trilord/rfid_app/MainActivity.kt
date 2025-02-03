package live.trilord.rfid_app


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rfidManager = RFIDManager(this)

        setContent {
            MainScreen(context = this, rfidManager = rfidManager)
        }
    }
}
