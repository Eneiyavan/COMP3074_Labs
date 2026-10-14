package ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.data.Customer
import ca.gbc.comp3074.sivaganesan_eneiyavan.lab4.data.CustomerDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CustomerViewModel(private val customerDao: CustomerDao) : ViewModel() {
    private val searchText = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val customers = searchText
        .flatMapLatest { query ->
            if (query.isBlank()) customerDao.getAllCustomers()
            else customerDao.searchCustomers(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addCustomer(name: String, age: Int, isActive: Boolean) {
        viewModelScope.launch {
            customerDao.insert(Customer(name = name, age = age, isActive = isActive))
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch { customerDao.delete(customer) }
    }

    fun searchCustomers(query: String) {
        searchText.value = query.trim()
    }

    fun clearSearch() {
        searchText.value = ""
    }
}
