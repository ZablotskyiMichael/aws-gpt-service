package com.example.gpt.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GptClient {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String GPT_3_5_TURBO = "gpt-3.5-turbo";
  private static final String DONE_STRING = "[DONE]";
  private static final String DATA_PREFIX = "data:";
  private static final int DATA_PREFIX_LENGTH = "data:".length();

  private final ObjectMapper objectMapper;

  @Value("${gpt.apiKey}")
  private String apiKey;

  private final OkHttpClient client = new OkHttpClient();

  public String getGPTResponse(String userInput, Consumer<String> resultConsumer) {
    RequestBody body = RequestBody.create(
        getRequestJson(userInput).toString(),
        MediaType.get("application/json; charset=utf-8")
    );

    Request request = new Request.Builder()
        .url(API_URL)
        .header("Authorization", "Bearer " + apiKey)
        .post(body)
        .build();

    StringBuilder result = new StringBuilder();
    try (Response response = client.newCall(request).execute()) {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
        String line;
        String data = null;
        while ((line = reader.readLine()) != null) {
          if (line.startsWith(DATA_PREFIX)) {
            data = line.substring(DATA_PREFIX_LENGTH).trim();
            if (DONE_STRING.equalsIgnoreCase(data)) {
              break;
            }
            try {
              ChatCompletionChunk chatCompletionChunk = objectMapper.readValue(data, ChatCompletionChunk.class);
              if (chatCompletionChunk.getChoices() != null) {
                String collect = chatCompletionChunk.getChoices().stream()
                    .map(c -> c.getMessage() != null ? c.getMessage().getContent() : "")
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(""));
                result.append(collect);
                resultConsumer.accept(result.toString());
              }
            } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
            }
          } else if (line.isEmpty()) {
            if (DONE_STRING.equalsIgnoreCase(data)) {
              break;
            }
          } else {
            throw new RuntimeException("Oops...");
          }
        }
      } catch (Exception e) {
        System.out.println("Oops... " + e);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return result.toString();
  }

  @NotNull
  private static JsonObject getRequestJson(String userInput) {
    JsonObject json = new JsonObject();
    json.addProperty("model", GPT_3_5_TURBO);
    json.addProperty("stream", true);
    JsonArray messages = new JsonArray();

    JsonObject systemMessage = new JsonObject();
    systemMessage.addProperty("role", "system");
    systemMessage.addProperty("content", "You are a helpful assistant.");
    messages.add(systemMessage);

    JsonObject userMessage = new JsonObject();
    userMessage.addProperty("role", "user");
    userMessage.addProperty("content", userInput);
    messages.add(userMessage);

    json.add("messages", messages);
    return json;
  }
}
