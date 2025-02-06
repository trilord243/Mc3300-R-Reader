package live.trilord.rfid_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*

import androidx.navigation.compose.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rfidManager = RFIDManager(this)

        setContent {
            AppNavigation(Context = this, rfidManager = rfidManager)
        }
    }
}



@Composable
fun AppNavigation(Context: Context, rfidManager: RFIDManager) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "test") {
        composable("test") { MainScreen(context = Context,rfidManager)  }

    }
}

