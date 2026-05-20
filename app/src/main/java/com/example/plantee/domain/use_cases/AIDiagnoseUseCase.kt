package com.example.plantee.domain.use_cases

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import com.example.plantee.data.repositories.PlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.ui.nav.DiagnosisInput
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import com.example.plantee.BuildConfig
import com.example.plantee.domain.model.AiDiagnosisResult
import org.json.JSONObject

class AIDiagnoseUseCase @Inject constructor(
    private val plantsRepository: PlantsRepository,
    private val routinesRepository: IRoutinesRepository,
    private val httpClient: HttpClient,
    @ApplicationContext private val context: Context
) {

    private val jsonFormatter: Json = Json { ignoreUnknownKeys = true }
    private fun processResponse(response: String): String {
        val responseJson = jsonFormatter.parseToJsonElement(response).jsonObject
        val extractedText = responseJson["choices"]
            ?.jsonArray?.getOrNull(0)
            ?.jsonObject?.get("message")
            ?.jsonObject?.get("content")
            ?.jsonPrimitive?.content ?: ""

        return extractedText
    }
    fun mockResponse(): AiDiagnosisResult? {
        val rawBody = "{\"choices\":[{\"content_filter_results\":{\"hate\":{\"filtered\":false,\"severity\":\"safe\"},\"protected_material_code\":{\"detected\":false,\"filtered\":false},\"protected_material_text\":{\"detected\":false,\"filtered\":false},\"self_harm\":{\"filtered\":false,\"severity\":\"safe\"},\"sexual\":{\"filtered\":false,\"severity\":\"safe\"},\"violence\":{\"filtered\":false,\"severity\":\"safe\"}},\"finish_reason\":\"stop\",\"index\":0,\"logprobs\":null,\"message\":{\"annotations\":[],\"content\":\"{\\n  \\\"isPlantRelated\\\": true,\\n  \\\"diagnosisDescription\\\": \\\"The white substance on the leaves of your baby tomatoes could be a sign of powdery mildew or a pest issue, such as whiteflies or mealybugs. The poor condition of the plant may be related to inadequate sunlight or other environmental stressors. The moisture level seems adequate, but ensure proper drainage to avoid root rot.\\\",\\n  \\\"proposedRoutines\\\": [\\n    {\\n      \\\"id\\\": 1,\\n      \\\"name\\\": \\\"Regular Inspection\\\",\\n      \\\"description\\\": \\\"Inspect the plants regularly for signs of pests or disease, especially on the undersides of leaves.\\\",\\n      \\\"activeDays\\\": 7,\\n      \\\"startDate\\\": \\\"2026-05-19\\\",\\n      \\\"endDate\\\": \\\"2026-06-19\\\"\\n   },\\n    {\\n      \\\"id\\\": 2,\\n      \\\"name\\\": \\\"Fungicide Application\\\",\\n      \\\"description\\\": \\\"If powdery mildew is confirmed, apply an appropriate fungicide to control the spread.\\\",\\n      \\\"activeDays\\\": 14,\\n      \\\"startDate\\\": \\\"2026-05-19\\\",\\n      \\\"endDate\\\": \\\"2026-06-19\\\"\\n   },\\n    {\\n      \\\"id\\\": 3,\\n      \\\"name\\\": \\\"Increase Sun Exposure\\\",\\n      \\\"description\\\": \\\"If possible, relocate the plants to an area with increased sunlight exposure or trim surrounding plants to allow more light.\\\",\\n      \\\"activeDays\\\": 7,\\n      \\\"startDate\\\": \\\"2026-05-19\\\",\\n      \\\"endDate\\\": \\\"2026-06-19\\\"\\n   }\\n  ]\\n}\",\"refusal\":null,\"role\":\"assistant\"}}],\"created\":1779223255,\"id\":\"chatcmpl-DhLXDfqRKqhKR0LpJrg4rr01uYej6\",\"model\":\"gpt-4o-mini-2024-07-18\",\"object\":\"chat.completion\",\"prompt_filter_results\":[{\"prompt_index\":0,\"content_filter_results\":{\"hate\":{\"filtered\":false,\"severity\":\"safe\"},\"jailbreak\":{\"detected\":false,\"filtered\":false},\"self_harm\":{\"filtered\":false,\"severity\":\"safe\"},\"sexual\":{\"filtered\":false,\"severity\":\"safe\"},\"violence\":{\"filtered\":false,\"severity\":\"safe\"}}}],\"service_tier\":\"default\",\"system_fingerprint\":\"fp_eb37e061ec\",\"usage\":{\"completion_tokens\":289,\"completion_tokens_details\":{\"accepted_prediction_tokens\":0,\"audio_tokens\":0,\"reasoning_tokens\":0,\"rejected_prediction_tokens\":0},\"latency_checkpoint\":{\"engine_tbt_ms\":11,\"engine_ttft_ms\":473,\"engine_ttlt_ms\":3756,\"pre_inference_ms\":139,\"service_tbt_ms\":11,\"service_ttft_ms\":1069,\"service_ttlt_ms\":4349,\"total_duration_ms\":4218,\"user_visible_ttft_ms\":931},\"prompt_tokens\":8831,\"prompt_tokens_details\":{\"audio_tokens\":0,\"cached_tokens\":0},\"total_tokens\":9120}}"
        val responseJson = processResponse(rawBody)

        val aiResult = try {
            jsonFormatter.decodeFromString<AiDiagnosisResult>(responseJson.trim())
        } catch (e: Exception) {
            Log.e("GitHubDebug", "Błąd parsowania JSON", e)
            null
        }

        return aiResult
    }

}
