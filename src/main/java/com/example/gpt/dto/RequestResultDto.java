package com.example.gpt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestResultDto {
  private String id;
  private String message;
  private boolean complete;
}
