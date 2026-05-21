package com.example.plantee.domain.use_cases

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import com.example.plantee.BuildConfig
import com.example.plantee.data.repositories.PlantsRepository
import com.example.plantee.domain.model.AiDiagnosisResult
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.ui.nav.DiagnosisInput
import com.example.plantee.utils.DayBitmaskHelper.toDaysList
import com.example.plantee.utils.ImageConverter.uriToOptimizedBase64
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.time.LocalDate
import javax.inject.Inject

class AIDiagnoseUseCase @Inject constructor(
    private val plantsRepository: PlantsRepository,
    private val routinesRepository: IRoutinesRepository,
    private val httpClient: HttpClient,
    @param:ApplicationContext private val context: Context
) {

    private val jsonFormatter: Json = Json { ignoreUnknownKeys = true }
    private val githubToken = BuildConfig.GITHUB_MODELS_API_KEY
    private val url = "https://models.inference.ai.azure.com/chat/completions"

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
            null
        }

        return aiResult
    }

    private fun createPrompt(diagnosisInput: DiagnosisInput, plant: Plant, connectedRoutines: List<Routine?>): String {
        val currentDate = LocalDate.now().toString()
        val currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags()

        val routinesContext = connectedRoutines.joinToString("\n") { routine ->
            "- ${routine?.name}: ${routine?.activeDays?.toDaysList()?.let { "Schedule [$it]," } } Lastly done: ${routine?.lastlyDoneAt?.let { "Last done: $it" } ?: "Not done yet" }}"
        }

        val diagnosesContext = plant.diagnoses.take(3).joinToString(separator = "\n") { summary ->
            "* At ${summary.diagnosedAt.toLocalDate()} user reported: ${summary.description ?: "Unknown"}"
        }

        return """
            [ROLE]
            You are an expert botanist and plant pathologist specializing in indoor and outdoor plant care.
        
            [CONTEXT]
            - Current date: $currentDate
            - Target Language for all text outputs: $currentLanguage
        
            [DATA FOR ANALYSIS]
            - Plant Species: ${plant.species}
            - User Input Text: "${diagnosisInput.problemDescription.replace("\"", "'")}"
            - Sun Level: ${diagnosisInput.sunLevel} (scale 0-1, where 0 is shade, 1 is full sun)
            - Moisture Level: ${diagnosisInput.moistureLevel} (scale 0-1, where 0 is dry, 1 is waterlogged)
            - Current Routines:
            $routinesContext
            - History:
            $diagnosesContext
            - Attached Image: [Analyze if provided]
        
            [OPERATIONAL FLOW]
            1. If the "User Input Text" or the image is about topics other than plants (such as cars, pets, gadgets, or general text questions unrelated to gardening), set "isPlantRelated" to false. In "diagnosisDescription", provide a brief message in $currentLanguage explaining that this system only analyzes plants. Return `[]` for "proposedRoutines".
            2. Otherwise, set "isPlantRelated" to true and create a plant care analysis.
        
            [DIAGNOSIS QUALITY RULES]
            - Address the user directly using natural language. Do not talk about the plant in the third person.
            - Name the specific suspected issue at the beginning (e.g., root rot, fungal leaf spot, spider mites). Do not use vague phrases like "your plant is sick".
            - Explain the cause by connecting the user's description with the Sun Level and Moisture Level, but do not write the exact numeric values in the response text.
            - Mention if the issue is new or recurring based on the history.
        
            [TECHNICAL FORMATTING RULES]
            - Output ONLY a valid JSON object. Do not use markdown blocks.
            - All string fields must be written entirely in $currentLanguage.
            - Dates must follow the strict 'YYYY-MM-DD' format.
        
            [ROUTINE CONFIGURATION RULES]
            A "routine" is a highly specific, repetitive task that the user will check off as "Done" on specific days of the week. 
            - **Never give generic advice**: Do NOT use vague names like "Adjust water scheduling" or "Fix lighting".
            - **Micro-tasks only**: Break down care into bite-sized, micro-actions.
            - **Frequency & Bitmask Logic**: 
              The `activeDays` field is a BITMASK integer representing specific days of the week. 
              Monday = 1 (1 shl 0), Tuesday = 2 (1 shl 1), Wednesday = 4 (1 shl 2), Thursday = 8 (1 shl 3), Friday = 16 (1 shl 4), Saturday = 32 (1 shl 5), Sunday = 64 (1 shl 6).
              
              CRITICAL FREQUENCY MATCHING:
              - If a task is WEEKLY, you must choose EXACTLY ONE appropriate day of the week. The `activeDays` value must be exactly one of these: 1, 2, 4, 8, 16, 32, or 64. Never select multiple days for a weekly routine.
              - If a task is EVERY 3 DAYS, select exactly 2 days (e.g., Monday and Thursday -> 1 + 8 = 9).
              - If a task is DAILY, select all days (1+2+4+8+16+32+64 = 127).
              - Alternate the days for different routines! Do not put all proposed routines on the exact same days. If watering check is on Monday, put fungal spray on Wednesday.
              
        
            Guidelines for "proposedRoutines":
            - **Names**: Use short, action-focused names in $currentLanguage (e.g., "Weekly Soil Check").
            - **Description**: Provide clear instructions using imperative command verbs (e.g., "Check", "Water").
        
            [JSON SCHEMA]
            {
              "isPlantRelated": true/false,
              "diagnosisDescription": "A warm, clear explanation addressed directly to the user in $currentLanguage, or a simple native system message if the topic is not plant related.",
              "proposedRoutines": [
                {
                  "id": 1,
                  "name": "Action name in $currentLanguage",
                  "description": "One single clear command in $currentLanguage telling the user exactly what to do.",
                  "activeDays": 4, 
                  "startDate": "YYYY-MM-DD",
                  "endDate": "YYYY-MM-DD"
                }
              ]
            }    
        """.trimIndent()
    }

    suspend operator fun invoke(diagnosisInput: DiagnosisInput): AiDiagnosisResult? {
        val plant = plantsRepository.getPlantOnce(diagnosisInput.plantId) ?: return null
        val connectedRoutines = routinesRepository.getRoutinesForPlant(plant.id)
        val base64Image = diagnosisInput.imageUri?.let { uriToOptimizedBase64(context, it.toUri()) }

        val prompt = createPrompt(diagnosisInput, plant, connectedRoutines)
        val payload = buildJsonObject {
            put("model", "gpt-4o-mini")
            put("messages", buildJsonArray {
                add(buildJsonObject {
                    put("role", "user")
                    put("content", buildJsonArray {
                        add(buildJsonObject {
                            put("type", "text")
                            put("text", prompt)
                        })
                        if (base64Image != null) {
                            add(buildJsonObject {
                                put("type", "image_url")
                                put("image_url", buildJsonObject {
                                    put("url", "data:image/jpeg;base64,$base64Image")
                                })
                            })
                        }
                    })
                })
            })
            put("response_format", buildJsonObject {
                put("type", "json_object")
            })
        }

        val responseText = try {
            val httpResponse = httpClient.post(url) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $githubToken")
                setBody(payload.toString())
            }

            val rawBody = httpResponse.bodyAsText()
            val extractedText = processResponse(rawBody)

            extractedText
        } catch (e: Exception) {
            return null
        }

        return try {
            jsonFormatter.decodeFromString<AiDiagnosisResult>(responseText.trim())
        } catch (e: Exception) {
            return null
        }
    }
}
