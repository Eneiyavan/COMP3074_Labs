package ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.data.AppDatabase
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.data.Customer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            MaterialTheme {
                val customerViewModel: CustomerViewModel = viewModel(
                    factory = CustomerViewModelFactory(database.customerDao()),
                )
                CustomerApp(customerViewModel)
            }
        }
    }
}

@Composable
private fun CustomerApp(viewModel: CustomerViewModel) {
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        CustomerScreen(
            customers = customers,
            onAddCustomer = viewModel::addCustomer,
            onDeleteCustomer = viewModel::deleteCustomer,
            onSearch = viewModel::searchCustomers,
            onClearSearch = viewModel::clearSearch,
        )
    }
}

@Composable
private fun CustomerScreen(
    customers: List<Customer>,
    onAddCustomer: (String, Int, Boolean) -> Unit,
    onDeleteCustomer: (Customer) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var ageText by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Room Customer Demo", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Customer name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = ageText,
            onValueChange = { value -> if (value.all(Char::isDigit)) ageText = value },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Active customer")
            Spacer(Modifier.width(8.dp))
            Switch(checked = isActive, onCheckedChange = { isActive = it })
        }
        Button(
            onClick = {
                val age = ageText.toIntOrNull()
                if (name.isBlank() || age == null) {
                    message = "Enter a valid name and age."
                } else {
                    onAddCustomer(name.trim(), age, isActive)
                    name = ""
                    ageText = ""
                    isActive = false
                    message = "Customer submitted."
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Add Customer") }

        message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search by name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSearch(searchText) }, modifier = Modifier.weight(1f)) {
                Text("Search")
            }
            OutlinedButton(
                onClick = {
                    searchText = ""
                    onClearSearch()
                },
                modifier = Modifier.weight(1f),
            ) { Text("Clear") }
        }

        CustomerList(
            customers = customers,
            onDeleteCustomer = onDeleteCustomer,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CustomerList(
    customers: List<Customer>,
    onDeleteCustomer: (Customer) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (customers.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("No customers in the database.")
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = customers, key = Customer::id) { customer ->
                CustomerRow(customer = customer, onDelete = { onDeleteCustomer(customer) })
            }
        }
    }
}

@Composable
private fun CustomerRow(customer: Customer, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(customer.name, style = MaterialTheme.typography.titleMedium)
                Text("ID: ${customer.id}")
                Text("Age: ${customer.age}")
                Text(if (customer.isActive) "Active customer" else "Inactive customer")
            }
            TextButton(onClick = onDelete) { Text("Delete") }
        }
    }
}
