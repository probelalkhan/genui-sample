package dev.belalkhan.genui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.belalkhan.genui.ui.theme.GenUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GenUITheme {
                BookExtractionScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookExtractionScreen(
    viewModel: BookExtractionViewModel = viewModel()
) {
    val inputText by viewModel.inputText.collectAsState()
    val extractionState by viewModel.extractionState.collectAsState()

    // Define modern gradient colors
    val gradientColors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFf093fb)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Library Book Parser",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF667eea),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = 2000f
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Input Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Natural Language Input",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF667eea)
                        )

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { viewModel.updateInputText(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp),
                            placeholder = {
                                Text(
                                    "Enter book borrowing request...",
                                    color = Color.Gray.copy(alpha = 0.6f)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF667eea),
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                                cursorColor = Color(0xFF667eea)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            onClick = { viewModel.extractBookInfo() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF667eea)
                            ),
                            enabled = extractionState !is ExtractionState.Loading
                        ) {
                            if (extractionState is ExtractionState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(12.dp))
                            }
                            Text(
                                if (extractionState is ExtractionState.Loading) "Extracting..." else "Extract Information",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Output Section
                AnimatedVisibility(
                    visible = extractionState is ExtractionState.Success || extractionState is ExtractionState.Error,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                if (extractionState is ExtractionState.Success) "Structured JSON Output" else "Error",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (extractionState is ExtractionState.Success) 
                                    Color(0xFF10b981) else Color(0xFFef4444)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1e293b))
                                    .padding(16.dp)
                            ) {
                                when (extractionState) {
                                    is ExtractionState.Success -> {
                                        Text(
                                            text = formatJsonWithColors((extractionState as ExtractionState.Success).jsonOutput),
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp
                                        )
                                    }
                                    is ExtractionState.Error -> {
                                        Text(
                                            text = (extractionState as ExtractionState.Error).message,
                                            color = Color(0xFFfca5a5),
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 14.sp
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }

                // Info Card
                if (extractionState is ExtractionState.Idle) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.85f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "ℹ️ How it works",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF667eea)
                            )
                            Text(
                                "Enter a natural language request about borrowing a book, and Gemini 2.0 Flash Lite will extract structured information including:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                "• Book Name\n• Author\n• From Date\n• Till Date",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun formatJsonWithColors(json: String): androidx.compose.ui.text.AnnotatedString {
    return buildAnnotatedString {
        val lines = json.lines()
        lines.forEach { line ->
            when {
                line.contains(":") -> {
                    val parts = line.split(":", limit = 2)
                    // Key (blue-ish)
                    withStyle(SpanStyle(color = Color(0xFF60a5fa))) {
                        append(parts[0])
                    }
                    append(":")
                    if (parts.size > 1) {
                        // Value (green-ish for strings, yellow for others)
                        val value = parts[1].trim()
                        if (value.startsWith("\"")) {
                            withStyle(SpanStyle(color = Color(0xFF34d399))) {
                                append(parts[1])
                            }
                        } else {
                            withStyle(SpanStyle(color = Color(0xFFfbbf24))) {
                                append(parts[1])
                            }
                        }
                    }
                }
                line.trim() == "{" || line.trim() == "}" -> {
                    withStyle(SpanStyle(color = Color(0xFFa78bfa))) {
                        append(line)
                    }
                }
                else -> {
                    withStyle(SpanStyle(color = Color.White)) {
                        append(line)
                    }
                }
            }
            append("\n")
        }
    }
}