package xyz.kandrac.assignment.ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.kandrac.assignment.model.ScratchCard

@Composable
fun WidgetScratchCard(
    card: ScratchCard,
    modifier: Modifier = Modifier
) {
    val state = animateFloatAsState(if (card !is ScratchCard.UnscratchedScratchCard) 1f else 0f)

    Box(
        Modifier
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .aspectRatio(2f)
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.align(Alignment.Center)) {
            Text(card.id, fontSize = 20.sp, textAlign = TextAlign.Center)
        }
        Box(Modifier.fillMaxHeight().fillMaxWidth(state.value).background(Color.Black))
    }
}

@Composable
@Preview
private fun WidgetScratchCardPreview() {
    WidgetScratchCard(
        ScratchCard.UnscratchedScratchCard("ABCDEFGH") {}
    )
}