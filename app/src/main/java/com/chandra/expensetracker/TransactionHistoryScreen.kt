package com.chandra.expensetracker

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class HistoryTransactionItem(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: String,
    val time: String,
    val paymentMethod: String,
    val status: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // Handle device gesture & button backpress
    BackHandler(onBack = onBackClick)

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Income", "Expense"
    var selectedTransaction by remember { mutableStateOf<HistoryTransactionItem?>(null) }

    // Mock Comprehensive Transaction History Dataset
    val allTransactions = remember {
        listOf(
            HistoryTransactionItem(
                id = "TXN-984210",
                title = "Monthly Salary Deposit",
                category = "Income",
                amount = 85000.0,
                isIncome = true,
                date = "15 Aug 2026",
                time = "09:30 AM",
                paymentMethod = "Direct Bank Transfer",
                status = "Completed",
                icon = Icons.Default.ArrowUpward,
                iconBgColor = Color(0xFFE8F5E9),
                iconTint = Color(0xFF2E7D32)
            ),
            HistoryTransactionItem(
                id = "TXN-984209",
                title = "Supermarket Groceries",
                category = "Food & Dining",
                amount = 3450.0,
                isIncome = false,
                date = "14 Aug 2026",
                time = "06:45 PM",
                paymentMethod = "HDFC Credit Card",
                status = "Completed",
                icon = Icons.Default.ShoppingCart,
                iconBgColor = Color(0xFFFFF3E0),
                iconTint = Color(0xFFE65100)
            ),
            HistoryTransactionItem(
                id = "TXN-984208",
                title = "Freelance UI Design Project",
                category = "Income",
                amount = 25000.0,
                isIncome = true,
                date = "12 Aug 2026",
                time = "02:15 PM",
                paymentMethod = "UPI (Google Pay)",
                status = "Completed",
                icon = Icons.Default.AccountBalanceWallet,
                iconBgColor = Color(0xFFE8F5E9),
                iconTint = Color(0xFF2E7D32)
            ),
            HistoryTransactionItem(
                id = "TXN-984207",
                title = "Electronics Store Gadgets",
                category = "Shopping",
                amount = 12999.0,
                isIncome = false,
                date = "10 Aug 2026",
                time = "04:20 PM",
                paymentMethod = "Axis Debit Card",
                status = "Completed",
                icon = Icons.Default.Devices,
                iconBgColor = Color(0xFFE1F5FE),
                iconTint = Color(0xFF0288D1)
            ),
            HistoryTransactionItem(
                id = "TXN-984206",
                title = "Electric Utility Bill",
                category = "Bills & Utilities",
                amount = 2150.0,
                isIncome = false,
                date = "08 Aug 2026",
                time = "11:00 AM",
                paymentMethod = "Auto Debit",
                status = "Completed",
                icon = Icons.Default.Bolt,
                iconBgColor = Color(0xFFFFF8E1),
                iconTint = Color(0xFFF57F17)
            ),
            HistoryTransactionItem(
                id = "TXN-984205",
                title = "Coffee & Bakery Snack",
                category = "Food & Dining",
                amount = 480.0,
                isIncome = false,
                date = "06 Aug 2026",
                time = "05:30 PM",
                paymentMethod = "UPI (PhonePe)",
                status = "Completed",
                icon = Icons.Default.Restaurant,
                iconBgColor = Color(0xFFF3E5F5),
                iconTint = Color(0xFF8E24AA)
            ),
            HistoryTransactionItem(
                id = "TXN-984204",
                title = "Stock Dividend Yield",
                category = "Income",
                amount = 4200.0,
                isIncome = true,
                date = "04 Aug 2026",
                time = "10:00 AM",
                paymentMethod = "Zerodha Demat",
                status = "Completed",
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                iconBgColor = Color(0xFFE8F5E9),
                iconTint = Color(0xFF2E7D32)
            ),
            HistoryTransactionItem(
                id = "TXN-984203",
                title = "Annual Fitness Membership",
                category = "Health & Fitness",
                amount = 15000.0,
                isIncome = false,
                date = "01 Aug 2026",
                time = "08:15 AM",
                paymentMethod = "ICICI Credit Card",
                status = "Completed",
                icon = Icons.Default.FitnessCenter,
                iconBgColor = Color(0xFFFFEBEE),
                iconTint = Color(0xFFC62828)
            ),
            HistoryTransactionItem(
                id = "TXN-984202",
                title = "Netflix & Spotify Subscription",
                category = "Entertainment",
                amount = 999.0,
                isIncome = false,
                date = "28 Jul 2026",
                time = "12:00 PM",
                paymentMethod = "Auto Debit",
                status = "Completed",
                icon = Icons.Default.Movie,
                iconBgColor = Color(0xFFEDE7F6),
                iconTint = Color(0xFF512DA8)
            )
        )
    }

    // Filter logic
    val filteredTransactions = remember(searchQuery, selectedFilter) {
        allTransactions.filter { item ->
            val matchesFilter = when (selectedFilter) {
                "Income" -> item.isIncome
                "Expense" -> !item.isIncome
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true) ||
                    item.id.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    val totalIncome = remember(allTransactions) {
        allTransactions.filter { it.isIncome }.sumOf { it.amount }
    }
    val totalExpense = remember(allTransactions) {
        allTransactions.filter { !it.isIncome }.sumOf { it.amount }
    }
    val netBalance = totalIncome - totalExpense

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Transaction History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Exporting statement...", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Export Transactions",
                            tint = Color(0xFF673AB7)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Header Stats Summary Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = "Income",
                    amount = "₹${String.format("%.0f", totalIncome)}",
                    color = Color(0xFF2E7D32),
                    bgColor = Color(0xFFE8F5E9),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Expense",
                    amount = "₹${String.format("%.0f", totalExpense)}",
                    color = Color(0xFFC62828),
                    bgColor = Color(0xFFFFEBEE),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Net Balance",
                    amount = "₹${String.format("%.0f", netBalance)}",
                    color = Color(0xFF673AB7),
                    bgColor = Color(0xFFEDE7F6),
                    modifier = Modifier.weight(1f)
                )
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                placeholder = { Text("Search title, category, or ID...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = Color(0xFF673AB7),
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("All", "Income", "Expense").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF673AB7),
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            selectedBorderColor = Color(0xFF673AB7)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Transactions Count Header
            Text(
                text = "${filteredTransactions.size} Transactions Found",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Transactions List or Empty State
            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Transactions Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Try adjusting your search query or category filter.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(filteredTransactions, key = { it.id }) { item ->
                        TransactionRowItem(
                            item = item,
                            onClick = { selectedTransaction = item }
                        )
                    }
                }
            }
        }
    }

    // Detail Dialog when a transaction is clicked
    selectedTransaction?.let { item ->
        AlertDialog(
            onDismissRequest = { selectedTransaction = null },
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(item.iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = item.iconTint,
                        modifier = Modifier.size(30.dp)
                    )
                }
            },
            title = {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Amount display
                    Text(
                        text = if (item.isIncome) "+₹${String.format("%,.2f", item.amount)}" else "-₹${String.format("%,.2f", item.amount)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    DetailRow(label = "Transaction ID", value = item.id)
                    DetailRow(label = "Category", value = item.category)
                    DetailRow(label = "Date & Time", value = "${item.date}, ${item.time}")
                    DetailRow(label = "Payment Method", value = item.paymentMethod)
                    DetailRow(label = "Status", value = item.status, isStatus = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedTransaction = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Receipt shared for ${item.id}", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Receipt")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = color.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = amount,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun TransactionRowItem(
    item: HistoryTransactionItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(item.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.category,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = " • ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = item.date,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (item.isIncome) "+₹${String.format("%,.0f", item.amount)}" else "-₹${String.format("%,.0f", item.amount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF4CAF50).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = item.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isStatus: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        if (isStatus) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFF4CAF50).copy(alpha = 0.15f)
            ) {
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        } else {
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
