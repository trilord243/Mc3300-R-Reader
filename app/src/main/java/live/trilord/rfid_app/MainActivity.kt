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
            AppNavigation(context = this, rfidManager = rfidManager)
        }
    }
}




@Composable
fun AppNavigation(context: Context, rfidManager: RFIDManager) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(context = context, rfidManager, navController) }
        composable("write") { WriteScreen(context = context, rfidManager, navController) }
    }
}

