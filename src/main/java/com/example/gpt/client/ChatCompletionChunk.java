package com.example.gpt.client;

import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionChunk {
  String id;

  /**
   * The type of object returned, should be "chat.completion.chunk"
   */
  String object;

  /**
   * The creation time in epoch seconds.
   */
  long created;

  /**
   * The model used.
   */
  String model;

  /**
   * A list of all generated completions.
   */
  List<ChatCompletionChoice> choices;
}
