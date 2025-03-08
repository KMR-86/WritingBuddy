package com.example.WritingBuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.WritingBuddy.config.HuggingFaceConfig;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class HuggingFaceService {
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final HuggingFaceConfig config;

    @Autowired
    public HuggingFaceService(HuggingFaceConfig config) {
        this.config = config;
    }

    public String checkGrammar(String text) {
        return makeInferenceCall("gpt2-medium",
                "You are a writing assistant. Analyze this text for grammar and suggest improvements: " + text);
    }

    public String getAdditionalIdeas(String text) {
        return makeInferenceCall("gpt2-medium",
                "You are a writing assistant. Suggest additional ideas to expand this text: " + text);
    }

    public String getCounterArguments(String text) {
        return makeInferenceCall("gpt2-medium",
                "You are a writing assistant. Provide counter-arguments to this text: " + text);
    }

    private String makeInferenceCall(String model, String prompt) {
        String url = config.getApiUrl() + model;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String formattedPrompt = String.format("<s>[INST] %s [/INST]", prompt);

        JSONObject parameters = new JSONObject();
        parameters.put("temperature", 0.7);
        parameters.put("max_length", 500);
        parameters.put("return_full_text", false);
        parameters.put("max_new_tokens", 250);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("inputs", formattedPrompt);
        jsonBody.put("parameters", parameters);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(jsonBody.toString(), JSON))
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                return "Error details: " + responseBody;
            }
            return responseBody; // Return the already read response body
        } catch (IOException e) {
            return "Error making inference call: " + e.getMessage();
        }
    }

}
