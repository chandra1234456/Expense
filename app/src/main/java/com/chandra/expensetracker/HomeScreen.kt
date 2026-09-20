package com.chandra.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon



data class Transaction(
    val id: Int,
    val title: String,
    val category: String,
    val amount: String,
    val isIncome: Boolean,
    val time: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onAddExpenseClick: () -> Unit, onProfileClick: () -> Unit) {
    val transactions = listOf(
        Transaction(
            id = 1,
            title = "Salary Reward",
            category = "Income",
            amount = "₹50,000.00",
            isIncome = true,
            time = "Today, 09:00 AM",
            icon = Icons.Filled.KeyboardArrowUp,
            iconBgColor = Color(0xFFE8F5E9),
            iconTint = Color(0xFF2E7D32)
        ),
        Transaction(
            id = 2,
            title = "Grocery Shopping",
            category = "Food & Drinks",
            amount = "₹1,550.00",
            isIncome = false,
            time = "Today, 11:30 AM",
            icon = Icons.Default.ShoppingCart,
            iconBgColor = Color(0xFFFFF3E0),
            iconTint = Color(0xFFEF6C00)
        ),
        Transaction(
            id = 3,
            title = "Rent Payment",
            category = "Housing",
            amount = "₹12,000.00",
            isIncome = false,
            time = "Yesterday, 06:00 PM",
            icon = Icons.Default.Home,
            iconBgColor = Color(0xFFE8EAF6),
            iconTint = Color(0xFF283593)
        ),
        Transaction(
            id = 4,
            title = "Netflix Premium",
            category = "Entertainment",
            amount = "₹649.00",
            isIncome = false,
            time = "24 May, 08:00 AM",
            icon = Icons.Default.PlayArrow,
            iconBgColor = Color(0xFFFFEBEE),
            iconTint = Color(0xFFC62828)
        ),
        Transaction(
            id = 5,
            title = "Gym Membership",
            category = "Health & Fitness",
            amount = "₹1,200.00",
            isIncome = false,
            time = "22 May, 07:00 AM",
            icon = Icons.Default.Favorite,
            iconBgColor = Color(0xFFF3E5F5),
            iconTint = Color(0xFF6A1B9A)
        )
    )

    val balanceAnimatable = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit) {
        balanceAnimatable.animateTo(
            targetValue = 48250f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1400,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
    }
    val formattedBalance = String.format("₹%,.2f", balanceAnimatable.value)

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Expense",
                        tint = Color.White
                    )
                },
                text = {
                    Text(
                        text = "Add Expense",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Hello, ${UserManager.userName}!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Manage your budget smartly",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Balance Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total Balance",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = formattedBalance,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                FinanceBadge(
                                    label = "Income",
                                    amount = "₹50,000.00",
                                    isIncome = true
                                )
                                FinanceBadge(
                                    label = "Expenses",
                                    amount = "₹1,750.00",
                                    isIncome = false
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Recent Transactions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(transactions) { transaction ->
                TransactionItem(transaction)
            }

            item {
                Spacer(modifier = Modifier.height(72.dp)) // Extra space to clear the FAB
            }
        }
    }
}

@Composable
fun FinanceBadge(label: String, amount: String, isIncome: Boolean) {
    Row(
        modifier = Modifier
            .background(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = if (isIncome) Color(0xFF4CAF50).copy(alpha = 0.2f) else Color(0xFFF44336).copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isIncome) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = if (isIncome) Color(0xFF81C784) else Color(0xFFEF9A9A),
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuickActionButton(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = color.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(transaction.iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transaction.icon,
                    contentDescription = null,
                    tint = transaction.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = transaction.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = (if (transaction.isIncome) "+" else "-") + transaction.amount,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (transaction.isIncome) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}