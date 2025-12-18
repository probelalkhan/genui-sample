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
        val prompt = buildPrompt(naturalLanguageInput)
        val response = generativeModel.generateContent(prompt)
        val responseText = response.text ?: throw Exception("No response from AI")

        // Extract JSON from response (remove markdown code blocks if present)
        val jsonText = cleanJsonResponse(responseText)

        // Validate JSON by parsing it
        json.decodeFromString<BookRequest>(jsonText)

        // Format JSON nicely
        return formatJson(jsonText)
    }

    private fun buildPrompt(input: String): String {
        return """
            Extract book borrowing information from the following text and return ONLY a valid JSON object with these exact fields:
            - bookName: the title of the book
            - author: the author of the book
            - from: the start date in YYYY-MM-DD format
            - till: the end date in YYYY-MM-DD format
            
            If a field cannot be determined, use an empty string "".
            Return ONLY the JSON object, nothing else.
            
            Text: $input
        """.trimIndent()
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
