package ca.gbc.comp3074.sivaganesan_eneiyavan.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ScreenTint = Color(0xFFFFF8FF)
private val SenderColor = Color(0xFF625F70)
private val OutlineRed = Color(0xFFFF151D)

private data class Message(val sender: String, val text: String)

private val messages = listOf(
    Message("Joe", "Hi!"),
    Message("Jim", "How are you?"),
    Message("Joe", "Test..1..2...3"),
    Message("Joe", "I hate coding!!!"),
    Message("Joe", "Hi!"),
    Message("Jim", "How are you?"),
    Message("Joe", "Test..1..2...3"),
    Message("Joe", "I hate coding!!!"),
    Message("Joe", "Hi!"),
    Message("Jim", "How are you?"),
    Message("Joe", "Test..1..2...3"),
    Message("Joe", "I hate coding!!!")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(250, 250, 250)
        window.navigationBarColor = android.graphics.Color.rgb(250, 250, 250)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = ScreenTint) {
                    MessageList()
                }
            }
        }
    }
}

@Composable
private fun MessageList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        items(messages) { message -> MessageRow(message) }
    }
}

@Composable
private fun MessageRow(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        AndroidAvatar(modifier = Modifier.size(56.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message.sender,
                color = SenderColor,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Surface(
                color = ScreenTint,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0EDF2))
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    color = Color(0xFF26252B),
                    fontSize = 19.sp,
                    lineHeight = 23.sp
                )
            }
        }
    }
}

@Composable
private fun AndroidAvatar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val ringWidth = 2.5.dp.toPx()
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(color = OutlineRed, radius = size.minDimension / 2f - ringWidth / 2f, style = Stroke(ringWidth))

        // A simple pale Android head and angular body, echoing the placeholder avatar in the reference.
        val cx = center.x
        val cy = center.y + size.height * 0.02f
        val headWidth = size.width * 0.38f
        val headHeight = size.height * 0.22f
        val headTop = cy - headHeight * 0.72f
        drawArc(
            color = Color.White,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(cx - headWidth / 2f, headTop),
            size = Size(headWidth, headHeight)
        )
        val body = Path().apply {
            moveTo(cx - headWidth / 2f, headTop + headHeight * 0.5f)
            lineTo(cx + headWidth / 2f, headTop + headHeight * 0.5f)
            lineTo(cx + size.width * 0.39f, cy + size.height * 0.40f)
            lineTo(cx + size.width * 0.10f, cy + size.height * 0.40f)
            close()
        }
        drawPath(body, Color(0xFFE8E7EB))
        drawLine(Color.White, Offset(cx - headWidth * .28f, headTop), Offset(cx - headWidth * .48f, headTop - headHeight * .42f), 1.dp.toPx())
        drawLine(Color.White, Offset(cx + headWidth * .28f, headTop), Offset(cx + headWidth * .48f, headTop - headHeight * .42f), 1.dp.toPx())
        val eyeY = headTop + headHeight * 0.70f
        drawCircle(Color(0xFFC3C3C8), 1.15.dp.toPx(), Offset(cx - headWidth * .23f, eyeY))
        drawCircle(Color(0xFFC3C3C8), 1.15.dp.toPx(), Offset(cx + headWidth * .23f, eyeY))
    }
}
