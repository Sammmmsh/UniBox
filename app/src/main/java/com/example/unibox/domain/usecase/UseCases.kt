package com.example.unibox.domain.usecase

import com.example.unibox.domain.model.Category
import com.example.unibox.domain.model.UniBoxItem
import com.example.unibox.domain.repository.UniBoxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Use cases encapsulate a single business action.
// They keep the ViewModel thin and the business logic testable.

class GetItemsUseCase @Inject constructor(
    private val repository: UniBoxRepository
) {
    operator fun invoke(category: Category? = null): Flow<List<UniBoxItem>> {
        return if (category != null) {
            repository.getItemsByCategory(category)
        } else {
            repository.getAllItems()
        }
    }
}

class SearchItemsUseCase @Inject constructor(
    private val repository: UniBoxRepository
) {
    operator fun invoke(query: String, category: Category? = null): Flow<List<UniBoxItem>> {
        return if (category != null) {
            repository.searchItemsByCategory(query, category)
        } else {
            repository.searchItems(query)
        }
    }
}

class SaveItemUseCase @Inject constructor(
    private val repository: UniBoxRepository
) {
    suspend operator fun invoke(item: UniBoxItem): Long {
        return repository.saveItem(item)
    }
}

class DeleteItemUseCase @Inject constructor(
    private val repository: UniBoxRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteItem(id)
    }
}

class UpdateItemUseCase @Inject constructor(
    private val repository: UniBoxRepository
) {
    suspend operator fun invoke(item: UniBoxItem) {
        repository.updateItem(item.copy(updatedAt = System.currentTimeMillis()))
    }
}
