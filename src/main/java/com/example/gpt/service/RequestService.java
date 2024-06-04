package com.example.gpt.service;

import com.example.gpt.client.GptClient;
import com.example.gpt.data.RequestResult;
import com.example.gpt.dto.RequestCreateDto;
import com.example.gpt.dto.RequestCreateResponseDto;
import com.example.gpt.dto.RequestResultDto;
import com.example.gpt.repository.RequestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class RequestService {
  private final GptClient gptClient;
  private final RequestResultRepository resultRepository;

  protected final ExecutorService threadPool = Executors.newFixedThreadPool(20);

  public RequestCreateResponseDto createRequest(RequestCreateDto dto) {
    RequestResult requestResult = resultRepository.save(
        RequestResult.builder()
            .request(dto.getInput())
            .completed(false)
            .build()
    );
    threadPool.execute(() -> {
      String result = gptClient.getGPTResponse(
          requestResult.getRequest(),
          (res) -> resultRepository.updateResult(requestResult.getId(), res, false));
      resultRepository.updateResult(requestResult.getId(), result, true);
    });
    return RequestCreateResponseDto.builder()
        .responseId(requestResult.getId())
        .build();
  }

  public RequestResultDto getRequestResult(String requestId) {
    return convert(resultRepository.getById(requestId));
  }

  private RequestResultDto convert(RequestResult requestResult) {
    if(requestResult == null){
      return RequestResultDto.builder().build();
    }
    return RequestResultDto.builder()
        .id(requestResult.getId())
        .message(requestResult.getResult())
        .complete(requestResult.isCompleted())
        .build();
  }
}
