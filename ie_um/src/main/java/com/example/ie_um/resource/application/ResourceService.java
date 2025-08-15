package com.example.ie_um.resource.application;

import com.example.ie_um.resource.AiServiceException;
import com.example.ie_um.resource.api.dto.request.HashTagReqDto;
import com.example.ie_um.resource.api.dto.response.ResourceListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {
    private final WebClient webClient;

    public ResourceListResDto forwardHashtags(HashTagReqDto request) {
        try {
            ResourceListResDto aiResponse = webClient.post()
                    .uri("/recommend")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ResourceListResDto.class)
                    .block();

            if (aiResponse == null) {
                throw new AiServiceException("AI 서버로부터 응답을 받지 못했습니다.");
            }

            return aiResponse;

        } catch (WebClientResponseException e) {
            log.error("AI 서버 응답 에러: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new AiServiceException("AI 추천 서비스에 문제가 발생했습니다.");

        } catch (Exception e) {
            log.error("AI 서버 호출 중 알 수 없는 에러 발생", e);
            throw new AiServiceException("AI 추천 서비스 호출 중 오류가 발생했습니다.");
        }
    }
}
