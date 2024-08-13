package eu.glitch.mywarehouse.ui.screens.product

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.IRepository
import eu.glitch.mywarehouse.database.impl.RepositoryImpl
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.utils.SeedDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: RepositoryImpl, @ApplicationContext context: Context
) : ViewModel(), ProductListActions {




    init {

        viewModelScope.launch {
            Log.d("seed", "seed launch: ")
            SeedDatabase.seed(repository, context)
        }
    }


    var data: ProductListData = ProductListData()

    private val _productListUIState: MutableStateFlow<ProductListUIState> =
        MutableStateFlow(value = ProductListUIState.Loading)
    val productListUIState = _productListUIState.asStateFlow()


    fun loadProducts() {
        viewModelScope.launch {
            data.products = repository.getProducts().toMutableList()
            filterProducts()
            _productListUIState.update {
                ProductListUIState.ProductListChanged(data)
            }
        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            data.types = repository.getTypes().toMutableList()
            _productListUIState.update {
                ProductListUIState.ProductListChanged(data)
            }


        }
    }

    fun loadChips() {
        viewModelScope.launch {
            data.chips = repository.getChips().toMutableList()
            _productListUIState.update {
                ProductListUIState.ProductListChanged(data)
            }
        }
    }

    override fun editFilter(name: String, id: String, add: Boolean) {

        if (data.filters.containsKey(key = name)) {
            //if there is already key with name
            var nameFilters = data.filters.get(name)
            if (add) {
                nameFilters!!.add(id)
                data.filters[name] = nameFilters
            } else {
                nameFilters!!.remove(id)
                if (nameFilters.isEmpty()) {
                    data.filters.remove(name)
                } else {
                    data.filters[name] = nameFilters
                }
            }

        } else {
            //if not
            data.filters.put(name, mutableListOf(id))
        }
        Log.d("filter", "filters: ${data.filters}")
        viewModelScope.launch {
            filterProducts()
            _productListUIState.update {
                ProductListUIState.ProductListChanged(data)
            }
        }

    }

    private suspend fun filterProducts() {
        val filteredSet = mutableSetOf<Product>()
        data.filteredProducts.clear()

        if (data.filters.isEmpty()) {
            data.filteredProducts.addAll(data.products)
            return
        }

        data.products.forEach { product ->
            Log.e("TAG", "filterProducts: $product", )

            for (filter in data.filters) {
                if (filter.key == "typeId") {
                    if (filter.value.contains(product.typeId)) {
                        filteredSet.add(product)
                    }
                } else {
                    val chips = repository.getChipIdsForProduct(productId = product.id)
                    if (chips.any { it in filter.value }) {
                        filteredSet.add(product)
                    }


                }
            }
        }
        data.filteredProducts.addAll(filteredSet)
    }
}
