package com.example.gpt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCreateResponseDto {
  private String responseId;
}
