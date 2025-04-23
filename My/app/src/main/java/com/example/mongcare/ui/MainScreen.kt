package com.example.mongcare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("MongCare", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB2DFDB)
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "홈") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "설정") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("오늘도 건강하게!", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FeatureCard(
                    title = "건강 체크",
                    icon = Icons.Default.Favorite,
                    backgroundColor = Color(0xFFFFCDD2),
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    title = "기록 보기",
                    icon = Icons.Default.History,
                    backgroundColor = Color(0xFFBBDEFB),
                    modifier = Modifier.weight(1f)
                )
            }


            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FeatureCard(
                    title = "운동 추천",
                    icon = Icons.Default.Favorite,
                    backgroundColor = Color(0xFFFFCDD2),
                    modifier = Modifier.weight(1f)
                )
                FeatureCard(
                    title = "식단 관리",
                    icon = Icons.Default.History,
                    backgroundColor = Color(0xFFBBDEFB),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
    }
}
