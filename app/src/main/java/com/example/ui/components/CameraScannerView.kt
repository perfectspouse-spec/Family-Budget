package com.example.ui.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.model.ParsedReceipt
import com.example.data.ocr.ReceiptOcrService
import com.example.ui.i18n.StringsProvider
import java.io.InputStream
import java.util.concurrent.Executors

@Composable
fun CameraScannerDialog(
    strings: StringsProvider,
    isScanning: Boolean,
    onDismiss: () -> Unit,
    onImageCaptured: (Bitmap) -> Unit,
    onPresetSelected: (ParsedReceipt) -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    var showPermissionRationale by remember { mutableStateOf(!hasCameraPermission) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        showPermissionRationale = !isGranted
    }

    // Gallery Picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val bitmap = uriToBitmap(context, uri)
            if (bitmap != null) {
                onImageCaptured(bitmap)
            }
        }
    }

    if (showPermissionRationale && !hasCameraPermission) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = strings.cameraPermissionTitle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(text = strings.cameraPermissionDesc)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Veya alttaki hızlı örnek fiş şablonlarından birini seçerek anında test edebilirsiniz.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.testTag("grant_camera_permission_button")
                ) {
                    Text(strings.grantPermission)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("dismiss_permission_button")
                ) {
                    Text(strings.cancel)
                }
            }
        )
    }

    // Full screen camera overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("camera_scanner_fullscreen")
    ) {
        if (hasCameraPermission) {
            CameraPreviewContainer(
                onImageCaptured = onImageCaptured
            )
        } else {
            // Placeholder when permission not yet granted
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E292B)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF80CBC4),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fiş Tarayıcı Hazır",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Galeriden fiş görseli seçebilir veya hızlı örnek fişlerle OCR test edebilirsiniz.",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galeriden Fiş Seç")
                    }
                }
            }
        }

        // Animated Scanning Line Overlay
        ScanningReticleOverlay()

        // Top Controls: Close button & Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .background(Color(0x88000000), CircleShape)
                    .testTag("close_camera_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = strings.cancel,
                    tint = Color.White
                )
            }

            Surface(
                color = Color(0xAA004D40),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = strings.scanReceipt,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            IconButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier
                    .background(Color(0x88000000), CircleShape)
                    .testTag("gallery_pick_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Galeri",
                    tint = Color.White
                )
            }
        }

        // Bottom Controls: Preset Quick Bar & Capture
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xCC111E1D))
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isScanning) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF80CBC4),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = strings.scanningReceipt,
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            } else {
                Text(
                    text = "Fişi çerçevenin içine hizalayın",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CameraPreviewContainer(
    onImageCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    try {
                        cameraProviderFuture.get().unbindAll()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            cameraExecutor.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    } else if (cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        null
                    }

                    if (cameraSelector != null) {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Capture Floating Action Trigger
        IconButton(
            onClick = {
                imageCapture?.let { capture ->
                    capture.takePicture(
                        cameraExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                val bitmap = imageProxyToBitmap(imageProxy)
                                imageProxy.close()
                                if (bitmap != null) {
                                    ContextCompat.getMainExecutor(context).execute {
                                        onImageCaptured(bitmap)
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                exception.printStackTrace()
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
                .size(72.dp)
                .background(Color.White, CircleShape)
                .border(4.dp, Color(0xFF004D40), CircleShape)
                .testTag("take_receipt_photo_button")
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Fotoğraf Çek",
                tint = Color(0xFF004D40),
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

@Composable
fun ScanningReticleOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
    val lineProgress by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "line_pos"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val rectWidth = width * 0.85f
        val rectHeight = height * 0.55f
        val left = (width - rectWidth) / 2f
        val top = (height - rectHeight) / 2.5f

        // Draw darkened backdrop around receipt box
        drawRoundRect(
            color = Color(0x55000000),
            topLeft = Offset(0f, 0f),
            size = Size(width, height)
        )

        // Draw clear viewfinder rect
        drawRoundRect(
            color = Color(0xFF80CBC4),
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // Animated laser scanning line
        val scanY = top + (rectHeight * lineProgress)
        drawLine(
            color = Color(0xFF00E676),
            start = Offset(left + 10.dp.toPx(), scanY),
            end = Offset(left + rectWidth - 10.dp.toPx(), scanY),
            strokeWidth = 3.dp.toPx()
        )
    }
}

private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    return try {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options) ?: return null

        val rotation = image.imageInfo.rotationDegrees
        if (rotation != 0) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (rotated != bitmap) {
                bitmap.recycle()
            }
            rotated
        } else {
            bitmap
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        null
    }
}

private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        // Step 1: Decode bounds only to calculate inSampleSize
        val boundsOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOptions)
        }

        val maxDim = 1280
        var inSampleSize = 1
        val (rawWidth, rawHeight) = boundsOptions.outWidth to boundsOptions.outHeight
        if (rawHeight > maxDim || rawWidth > maxDim) {
            val halfHeight = rawHeight / 2
            val halfWidth = rawWidth / 2
            while ((halfHeight / inSampleSize) >= maxDim && (halfWidth / inSampleSize) >= maxDim) {
                inSampleSize *= 2
            }
        }

        // Step 2: Decode sampled bitmap
        val decodeOptions = BitmapFactory.Options().apply {
            this.inSampleSize = inSampleSize
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }

        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
