package com.example.gpt.controller;

import com.example.gpt.dto.RequestCreateDto;
import com.example.gpt.dto.RequestCreateResponseDto;
import com.example.gpt.dto.RequestResultDto;
import com.example.gpt.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class RequestController {

  private final RequestService requestService;

  @PostMapping
  public RequestCreateResponseDto createRequest(@RequestBody RequestCreateDto dto) {
    return requestService.createRequest(dto);
  }

  @GetMapping("/{requestId}")
  public RequestResultDto getById(@PathVariable String requestId) {
    return requestService.getRequestResult(requestId);
  }
}
