package com.example.gpt.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatCompletionChoice {
  Integer index;
  @JsonAlias("delta")
  ChatMessage message;

  /**
   * The reason why GPT stopped generating, for example "length".
   */
  @JsonProperty("finish_reason")
  String finishReason;
}
