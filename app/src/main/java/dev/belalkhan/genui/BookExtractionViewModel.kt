package dev.belalkhan.genui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val bookName: String = "",
    val author: String = "",
    val from: String = "",
    val till: String = ""
)

sealed class ExtractionState {
    data object Idle : ExtractionState()
    data object Loading : ExtractionState()
    data class Success(val jsonOutput: String) : ExtractionState()
    data class Error(val message: String) : ExtractionState()
}

class BookExtractionViewModel(
    private val geminiRepository: GeminiRepository = GeminiRepository()
) : ViewModel() {
    private val _extractionState = MutableStateFlow<ExtractionState>(ExtractionState.Idle)
    val extractionState: StateFlow<ExtractionState> = _extractionState.asStateFlow()

    private val _inputText = MutableStateFlow(
        "I want to borrow \"The Great Gatsby\" by F. Scott Fitzgerald from January 15th to January 30th."
    )
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun extractBookInfo() {
        viewModelScope.launch {
            try {
                _extractionState.value = ExtractionState.Loading

                val formattedJson = geminiRepository.extractBookInfo(_inputText.value)

                _extractionState.value = ExtractionState.Success(formattedJson)
            } catch (e: Exception) {
                _extractionState.value = ExtractionState.Error(
                    e.message ?: "Failed to extract book information"
                )
            }
        }
    }
}
