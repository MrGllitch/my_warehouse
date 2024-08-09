package eu.glitch.mywarehouse.ui.elements

import android.Manifest
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import eu.glitch.mywarehouse.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    takePhoto: (ImageCapture) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val cameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()




    LaunchedEffect(lensFacing) {
       // val cameraProvider = context.getCameraProvider()
       // cameraProvider.unbindAll()
       // cameraProvider.bindToLifecycle(
       //     lifecycleOwner,
       //     cameraSelector,
       //     preview,
       //     imageCapture
       // )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    if (cameraPermissionState.status.isGranted) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(0.9f)


            ) {
                AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

                IconButton(
                    modifier = Modifier.padding(bottom = 20.dp),
                    onClick = {
                        takePhoto(imageCapture)
                        onDismissRequest()
                    },
                    content = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.icon_photo_camera),
                            contentDescription = "Take picture",
                            tint = Color.White,
                            modifier = Modifier
                                .size(120.dp)
                                .border(1.dp, Color.White, CircleShape)
                                .padding(5.dp)
                        )
                    }
                )
            }
        }
    } else {
        SideEffect {
            cameraPermissionState.launchPermissionRequest()
        }
    }

}

//private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
//    suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }