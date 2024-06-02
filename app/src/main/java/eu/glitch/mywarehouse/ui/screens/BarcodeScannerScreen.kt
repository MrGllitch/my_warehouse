package eu.glitch.mywarehouse.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import eu.glitch.mywarehouse.navigation.INavigationRouter

@Composable
fun BarcodeScannerScreen(navigationRouter: INavigationRouter) {
    val context = LocalContext.current
    var scanResult by remember { mutableStateOf("") }
    var scanFlag by remember {
        mutableStateOf(false)
    }


    val compoundBarcodeView = remember {
        CompoundBarcodeView(context).apply {
            val capture = CaptureManager(context as Activity, this)
            capture.initializeFromIntent(context.intent, null)
            this.setStatusText("")
            capture.decode()
            this.decodeContinuous { result ->
                if (scanFlag) {
                    return@decodeContinuous
                }
                scanFlag = true
                result.text?.let {
                    scanResult = it
                    scanFlag = false
                }
            }
        }
    }
    SideEffect {
        compoundBarcodeView?.also {
            if (it.isAttachedToWindow) {
                it.resume()
            } else {
                it.pause()
            }
        }
    }
    if (scanResult != "") {
        compoundBarcodeView.pause()
        LaunchedEffect(scanResult) {
            navigationRouter.navigateFromBarcodeScannerScreen(scanResult)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { compoundBarcodeView },
        )
    }
}


