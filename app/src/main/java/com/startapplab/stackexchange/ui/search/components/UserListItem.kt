package com.startapplab.stackexchange.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.startapplab.stackexchange.ui.app.ProfileAvatar
import com.startapplab.stackexchange.ui.theme.ReputationBlueBackground
import com.startapplab.stackexchange.ui.theme.ReputationBlueText
import com.startapplab.stackexchange.ui.theme.ReputationGoldBackground
import com.startapplab.stackexchange.ui.theme.ReputationGoldText
import com.startapplab.stackexchange.ui.theme.ReputationGrayBackground
import com.startapplab.stackexchange.ui.theme.ReputationGrayText
import com.startapplab.stackexchange.ui.theme.ReputationGreenBackground
import com.startapplab.stackexchange.ui.theme.ReputationGreenText

@Composable
fun UserListItem(
    reputation: Int,
    username: String,
    profileImage: String?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            ProfileAvatar(
                profileImage = profileImage,
                username = username,
                size = 48.dp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Username
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Reputation Badge
            ReputationBadge(reputation = reputation)
        }
    }
}

@Composable
private fun ReputationBadge(reputation: Int) {
    val (backgroundColor, textColor) = getReputationColors(reputation)
    
    Box(
        modifier = Modifier
            .widthIn(min = 64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = formatReputation(reputation),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

private fun getReputationColors(reputation: Int): Pair<Color, Color> {
    return when {
        reputation >= 100_000 -> Pair(ReputationGoldBackground, ReputationGoldText)
        reputation >= 10_000 -> Pair(ReputationGreenBackground, ReputationGreenText)
        reputation >= 1_000 -> Pair(ReputationBlueBackground, ReputationBlueText)
        else -> Pair(ReputationGrayBackground, ReputationGrayText)
    }
}

private fun formatReputation(reputation: Int): String {
    return when {
        reputation >= 1_000_000 -> String.format("%.1fM", reputation / 1_000_000.0)
        reputation >= 10_000 -> String.format("%.1fk", reputation / 1_000.0)
        reputation >= 1_000 -> String.format("%.1fk", reputation / 1_000.0)
        else -> reputation.toString()
    }
}
