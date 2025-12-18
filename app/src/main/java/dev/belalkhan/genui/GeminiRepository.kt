package dev.belalkhan.genui

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.serialization.json.Json

class GeminiRepository {
    private val generativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.5-flash-lite")
    }

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * Extracts book borrowing information from natural language text
     * @param naturalLanguageInput The user's natural language request
     * @return Formatted JSON string with book information
     * @throws Exception if extraction fails
     */
    suspend fun extractBookInfo(naturalLanguageInput: String): String {
        TODO()
    }

    private fun buildPrompt(input: String): String {
        TODO()
    }

    private fun cleanJsonResponse(responseText: String): String {
        return responseText
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    private fun formatJson(jsonString: String): String {
        return try {
            val bookRequest = json.decodeFromString<BookRequest>(jsonString)
            json.encodeToString(BookRequest.serializer(), bookRequest)
        } catch (e: Exception) {
            jsonString
        }
    }
}
