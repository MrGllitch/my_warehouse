package eu.glitch.mywarehouse.ui.screens.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import eu.glitch.mywarehouse.R
import eu.glitch.mywarehouse.navigation.INavigationRouter
import eu.glitch.mywarehouse.navigation.NavigationDrawer
import eu.glitch.mywarehouse.ui.elements.GetIconByName
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navigationRouter: INavigationRouter) {

    val viewModel = hiltViewModel<ProductListViewModel>()

    var data by remember {
        mutableStateOf(ProductListData())
    }

    val state = viewModel.productListUIState.collectAsStateWithLifecycle()

    state.value.let {
        when (it) {
            ProductListUIState.Loading -> {
                viewModel.loadProducts()
                viewModel.loadTypes()
            }

            is ProductListUIState.ProductListChanged -> {
                data = it.data
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(drawerState = drawerState, navigationRouter = navigationRouter, content = {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.product_list)) },
                navigationIcon = {
                    val coroutineScope = rememberCoroutineScope()
                    IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                    }
                })
        },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navigationRouter.navigateToAddEditProductScreen(
                        null
                    )
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            }) {
            ProductListScreenContent(
                paddingValues = it,
                data = data,
                actions = viewModel,
                router = navigationRouter
            )
        }
    })
}


@Composable
fun ProductListScreenContent(
    paddingValues: PaddingValues,
    data: ProductListData,
    actions: ProductListActions,
    router: INavigationRouter
) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        data.products.forEach { product ->
            var type = data.types.find { it.id == product.typeId }
            item {
                Column(
                    modifier = Modifier
                        .border( // Outline
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { router.navigateToAddEditProductScreen(product.id) }

                        .padding(16.dp)

                ) {
                    Box {

                        if (product.imageName == "") {
                            Image(
                                painter = painterResource(id = R.drawable.image_placeholder),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(120.dp, 120.dp)
                                    .clip(RectangleShape)
                            )
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(File("${context.filesDir}/${product.imageName}")),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp, 120.dp)
                                    .clip(RectangleShape)

                            )
                        }
                        GetIconByName(
                            name = type!!.iconRoute,
                            contentDescription = "",
                            modifier = Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Text(text = product.name, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}


