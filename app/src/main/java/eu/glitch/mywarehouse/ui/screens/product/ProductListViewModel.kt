package eu.glitch.mywarehouse.ui.screens.product

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.glitch.mywarehouse.database.chip.ILocalChipRepository
import eu.glitch.mywarehouse.database.product.ILocalProductRepository
import eu.glitch.mywarehouse.database.type.ILocalTypeRepository
import eu.glitch.mywarehouse.database.unit.ILocalUnitRepository
import eu.glitch.mywarehouse.model.Product
import eu.glitch.mywarehouse.utils.SeedDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ILocalProductRepository,
    private val typeRepository: ILocalTypeRepository,
    private val unitRepository: ILocalUnitRepository,
    private val chipRepository: ILocalChipRepository,
    @ApplicationContext context: Context
) : ViewModel(), ProductListActions {




    init {

        viewModelScope.launch {
            Log.d("seed", "seed launch: ")
            SeedDatabase.seed(typeRepository, unitRepository, context)
        }
    }


    var data: ProductListData = ProductListData()

    private val _productListUIState: MutableStateFlow<ProductListUIState> =
        MutableStateFlow(value = ProductListUIState.Loading)
    val productListUIState = _productListUIState.asStateFlow()


    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                data.products = it.toMutableList()
                filterProducts()
                _productListUIState.update {
                    ProductListUIState.ProductListChanged(data)
                }
            }

        }
    }

    fun loadTypes() {
        viewModelScope.launch {
            typeRepository.getTypes().collect {
                data.types = it.toMutableList()
                _productListUIState.update {
                    ProductListUIState.ProductListChanged(data)
                }
            }

        }
    }

    fun loadChips() {
        viewModelScope.launch {
            chipRepository.getChips().collect {
                data.chips = it.toMutableList()
                _productListUIState.update {
                    ProductListUIState.ProductListChanged(data)
                }
            }

        }
    }

    override fun editFilter(name: String, id: Long, add: Boolean) {

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
            for (filter in data.filters) {
                if (filter.key == "typeId") {
                    if (filter.value.contains(product.typeId)) {
                        filteredSet.add(product)
                    }
                } else {

                    val chips =
                        productRepository.getChipIdsForProduct(productId = product.id!!)
                    if (chips.any { it in filter.value }) {
                        filteredSet.add(product)
                    }


                }
            }
        }
        data.filteredProducts.addAll(filteredSet)
    }
}
