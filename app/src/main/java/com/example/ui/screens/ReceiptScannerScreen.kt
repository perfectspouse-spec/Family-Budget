package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.viewmodel.BudgetUiState
import java.io.InputStream
import java.util.concurrent.Executors

@Composable
fun ReceiptScannerScreen(
    state: BudgetUiState,
    strings: StringsProvider,
    onClose: () -> Unit,
    onImageCaptured: (Bitmap) -> Unit,
    onPresetSelected: (ParsedReceipt) -> Unit,
    onManualEntryClick: () -> Unit = {}
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
    var isFlashOn by remember { mutableStateOf(false) }
    var useFrontCamera by remember { mutableStateOf(false) }

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
            onDismissRequest = { showPermissionRationale = false },
            title = {
                Text(
                    text = strings.cameraPermissionTitle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(text = strings.cameraPermissionDesc)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "İzin vermeden de galeriden fiş yükleyebilir veya hazır şablonlarla Gemini OCR test edebilirsiniz.",
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
                    onClick = { showPermissionRationale = false },
                    modifier = Modifier.testTag("dismiss_permission_button")
                ) {
                    Text(strings.cancel)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("receipt_scanner_screen")
    ) {
        if (hasCameraPermission) {
            CameraXLivePreview(
                isTorchOn = isFlashOn,
                useFrontCamera = useFrontCamera,
                onImageCaptured = onImageCaptured
            )
        } else {
            // Permission fallback banner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF101D1C)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF80CBC4),
                        modifier = Modifier.size(68.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "CameraX Fiş Tarayıcı",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kamera izni vererek fişlerinizi saniyeler içinde Gemini AI ile tarayabilir veya galeriden görsel seçebilirsiniz.",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kamera İzni İste")
                        }
                        OutlinedButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galeriden Seç")
                        }
                    }
                }
            }
        }

        // Viewfinder Guide Overlay & Laser animation
        ScannerViewfinderOverlay()

        // Top Control Bar: Close, Flash, Camera switch, Gallery
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 42.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .background(Color(0x99000000), CircleShape)
                    .testTag("close_camera_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = strings.cancel,
                    tint = Color.White
                )
            }

            Surface(
                color = Color(0xCC004D40),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF80CBC4),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Gemini OCR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasCameraPermission) {
                    IconButton(
                        onClick = { isFlashOn = !isFlashOn },
                        modifier = Modifier
                            .background(Color(0x99000000), CircleShape)
                            .testTag("toggle_flash_button")
                    ) {
                        Icon(
                            imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            contentDescription = "Flash",
                            tint = if (isFlashOn) Color(0xFFFFD54F) else Color.White
                        )
                    }

                    IconButton(
                        onClick = { useFrontCamera = !useFrontCamera },
                        modifier = Modifier
                            .background(Color(0x99000000), CircleShape)
                            .testTag("switch_camera_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Kamera Değiştir",
                            tint = Color.White
                        )
                    }
                }

                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .background(Color(0x99000000), CircleShape)
                        .testTag("gallery_pick_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Galeri",
                        tint = Color.White
                    )
                }
            }
        }

        // Bottom Dashboard: Scanning Progress & Quick Templates
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xEE0D1817))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isScanningOcr) {
                Surface(
                    color = Color(0xEE004D40),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF80CBC4),
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Gemini OCR Ayrıştırıyor...",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Mağaza, tarih, KDV ve iptal kalemleri okunuyor",
                                color = Color(0xFFB2DFDB),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            } else {
                val samplePresets = remember { ReceiptOcrService.getPresetReceipts() }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF80CBC4),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Fotoğraf Çekin veya Hızlı Örnek Fiş Seçin",
                            color = Color(0xFF80CBC4),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(samplePresets) { preset ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0x3380CBC4),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x6680CBC4)),
                                modifier = Modifier.clickable { onPresetSelected(preset) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Receipt,
                                        contentDescription = null,
                                        tint = Color(0xFF80CBC4),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = preset.merchantName.take(18),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("scanner_gallery_pick_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF80CBC4).copy(alpha = 0.6f))
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Galeriden Seç", color = Color.White, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onManualEntryClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("scanner_manual_entry_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C))
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Manuel Giriş", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Fişi çerçevenin içine net ve aydınlık şekilde hizalayın",
                color = Color.LightGray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun CameraXLivePreview(
    isTorchOn: Boolean,
    useFrontCamera: Boolean,
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
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
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

    LaunchedEffect(useFrontCamera) {
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

                    val targetSelector = if (useFrontCamera && cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    } else {
                        null
                    }

                    if (targetSelector != null) {
                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            targetSelector,
                            preview,
                            imageCapture
                        )
                        cameraControl = camera.cameraControl
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    LaunchedEffect(isTorchOn, cameraControl) {
        try {
            cameraControl?.enableTorch(isTorchOn)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Floating Camera Shutter Trigger
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
                .padding(bottom = 180.dp)
                .size(76.dp)
                .background(Color.White, CircleShape)
                .border(5.dp, Color(0xFF004D40), CircleShape)
                .testTag("take_receipt_photo_button")
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Fotoğraf Çek",
                tint = Color(0xFF004D40),
                modifier = Modifier.size(38.dp)
            )
        }
    }
}

@Composable
fun ScannerViewfinderOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
    val lineProgress by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "line_pos"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val rectWidth = width * 0.86f
        val rectHeight = height * 0.54f
        val left = (width - rectWidth) / 2f
        val top = (height - rectHeight) / 2.6f

        // Viewfinder rounded border
        drawRoundRect(
            color = Color(0xFF80CBC4),
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // Corner Highlights (Reticle Brackets)
        val cornerLen = 28.dp.toPx()
        val cornerStroke = 5.dp.toPx()
        val cornerColor = Color(0xFF00E676)

        // Top Left
        drawLine(cornerColor, Offset(left, top), Offset(left + cornerLen, top), cornerStroke)
        drawLine(cornerColor, Offset(left, top), Offset(left, top + cornerLen), cornerStroke)

        // Top Right
        drawLine(cornerColor, Offset(left + rectWidth, top), Offset(left + rectWidth - cornerLen, top), cornerStroke)
        drawLine(cornerColor, Offset(left + rectWidth, top), Offset(left + rectWidth, top + cornerLen), cornerStroke)

        // Bottom Left
        drawLine(cornerColor, Offset(left, top + rectHeight), Offset(left + cornerLen, top + rectHeight), cornerStroke)
        drawLine(cornerColor, Offset(left, top + rectHeight), Offset(left, top + rectHeight - cornerLen), cornerStroke)

        // Bottom Right
        drawLine(cornerColor, Offset(left + rectWidth, top + rectHeight), Offset(left + rectWidth - cornerLen, top + rectHeight), cornerStroke)
        drawLine(cornerColor, Offset(left + rectWidth, top + rectHeight), Offset(left + rectWidth, top + rectHeight - cornerLen), cornerStroke)

        // Laser scan line
        val scanY = top + (rectHeight * lineProgress)
        drawLine(
            color = Color(0xFF00E676),
            start = Offset(left + 12.dp.toPx(), scanY),
            end = Offset(left + rectWidth - 12.dp.toPx(), scanY),
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
