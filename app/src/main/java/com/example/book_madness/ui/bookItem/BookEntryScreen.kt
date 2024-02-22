package com.example.book_madness.ui.bookItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.book_madness.R
import com.example.book_madness.data.BookDetails
import com.example.book_madness.ui.AppViewModelFactoryProvider
import com.example.book_madness.ui.navigation.BookMadnessTitlesResId
import com.example.book_madness.ui.theme.AppTheme
import com.example.book_madness.util.BookMadnessTopAppBar
import kotlinx.coroutines.launch

@Composable
fun BookEntryScreen(
    navigateBack: () -> Unit,
    viewModel: BookEntryViewModel = viewModel(factory = AppViewModelFactoryProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            BookMadnessTopAppBar(
                title = stringResource(BookMadnessTitlesResId.BOOK_ADD_SCREEN)
            )
        }
    ) { innerPadding ->
        BookEntryBody(
            bookUiState = viewModel.bookUiState,
            onBookValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveBook()
                    navigateBack()
                }
            },
            ratingList = viewModel.ratingList,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun BookEntryBody(
    bookUiState: BookUiState,
    ratingList: List<String>,
    onBookValueChange: (BookDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large))
    ) {
        BookInputForm(
            bookDetails = bookUiState.bookDetails,
            onValueChange = onBookValueChange,
            ratingList = ratingList,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = bookUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun BookInputForm(
    bookDetails: BookDetails,
    ratingList: List<String>,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium))
    ) {
        OutlinedTextField(
            value = bookDetails.name,
            onValueChange = { onValueChange(bookDetails.copy(name = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            label = { Text(stringResource(R.string.book_name_required)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.genre,
            onValueChange = { onValueChange(bookDetails.copy(genre = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus() }
            ),
            label = { Text(stringResource(R.string.book_genre_required)) },
            modifier = modifier
        )

        RatingField(
            bookDetails = bookDetails,
            onValueChange = onValueChange,
            ratingList = ratingList
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.book_paper_format))
            Checkbox(
                checked = bookDetails.paper,
                onCheckedChange = { onValueChange(bookDetails.copy(paper = it)) }
            )
        }
        OutlinedTextField(
            value = bookDetails.startDate ?: "",
            onValueChange = { onValueChange(bookDetails.copy(startDate = it)) },
            label = { Text(stringResource(R.string.book_details_start_date)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.finishDate ?: "",
            onValueChange = { onValueChange(bookDetails.copy(finishDate = it)) },
            label = { Text(stringResource(R.string.book_details_finish_date)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.author ?: "",
            onValueChange = { onValueChange(bookDetails.copy(author = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            label = { Text(stringResource(R.string.book_author)) },
            modifier = modifier
        )
        OutlinedTextField(
            value = bookDetails.notes ?: "",
            onValueChange = { onValueChange(bookDetails.copy(notes = it)) },
            keyboardOptions =
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            label = { Text(stringResource(R.string.book_notes)) },
            singleLine = false,
            modifier = modifier
        )
        Text(
            text = stringResource(R.string.required_fields),
            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatingField(
    bookDetails: BookDetails,
    ratingList: List<String>,
    modifier: Modifier = Modifier,
    onValueChange: (BookDetails) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = bookDetails.rating ?: "",
            onValueChange = { onValueChange(bookDetails.copy(rating = it)) },
            label = { Text(stringResource(R.string.book_rating))  },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            }
        ) {
            ratingList.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onValueChange(bookDetails.copy(rating = selectionOption))
                        expanded = false
                        focusManager.clearFocus()
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
        Image(
            painter = painterResource(id = R.drawable.clear),
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.extra_large))
                .clickable {
                    onValueChange(bookDetails.copy(rating = null))
                    focusManager.clearFocus()
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookEntryScreenPreview() {
    AppTheme {
        BookEntryBody(
            bookUiState = BookUiState(
               BookDetails(
                   id = 1,
                   name = "Fourth Wing",
                   genre = "Fantasy",
                   paper = true,
                   rating = "4.5",
                   startDate = "02.11.2023",
                   finishDate = "23.01.2024",
                   notes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
               )
            ),
            onBookValueChange = { },
            onSaveClick = { },
            ratingList = emptyList()
        )
    }
}
