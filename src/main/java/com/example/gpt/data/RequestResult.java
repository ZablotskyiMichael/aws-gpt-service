package com.example.gpt.data;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@FieldNameConstants
public class RequestResult {

  @Id
  private String id;
  private String request;
  private String result;
  private boolean completed;
}
