package com.example.ie_um.resource.application;

import com.example.ie_um.resource.AiServiceException;
import com.example.ie_um.resource.api.dto.request.HashTagReqDto;
import com.example.ie_um.resource.api.dto.response.AiApiResponse;
import com.example.ie_um.resource.api.dto.response.ResourceListResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {
    private final WebClient webClient;
    public Mono<ResourceListResDto> forwardHashtags(HashTagReqDto request) {
        return webClient.post()
                .uri("/api/recommend")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                resp -> resp.bodyToMono(String.class).flatMap(body -> Mono.error(new AiServiceException("클라이언트 오류: " + body))))
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> resp.bodyToMono(String.class).flatMap(body -> Mono.error(new AiServiceException("서버 오류: " + body))))
                .bodyToMono(AiApiResponse.class)
                .timeout(Duration.ofSeconds(10)) // 10초 이상 걸리면 에러

                .map(aiResponse -> {
                    if (aiResponse == null || aiResponse.data() == null) {
                        throw new AiServiceException("AI 서버로부터 유효한 데이터를 받지 못했습니다.");
                    }
                    return aiResponse.data();
                })
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("AI 서버 응답 에러: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
                    return new AiServiceException("AI 추천 서비스에 문제가 발생했습니다.");
                })
                .onErrorMap(e -> {
                    log.error("AI 서버 호출 중 알 수 없는 에러 발생", e);
                    return new AiServiceException("AI 추천 서비스 호출 중 오류가 발생했습니다.");
                });
    }


//    public ResourceListResDto forwardHashtags(HashTagReqDto request) {
//        try {
//            AiApiResponse aiResponse = webClient.post()
//                    .uri("/api/recommend")
//                    .bodyValue(request)
//                    .retrieve()
//                    .bodyToMono(AiApiResponse.class)
//                    .block();
//
//            if (aiResponse == null || aiResponse.data() == null) {
//                throw new AiServiceException("AI 서버로부터 유효한 데이터를 받지 못했습니다.");
//            }
//
//            return aiResponse.data();
//
//        } catch (WebClientResponseException e) {
//            log.error("AI 서버 응답 에러: status={}, body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
//            throw new AiServiceException("AI 추천 서비스에 문제가 발생했습니다.");
//
//        } catch (Exception e) {
//            log.error("AI 서버 호출 중 알 수 없는 에러 발생", e);
//            throw new AiServiceException("AI 추천 서비스 호출 중 오류가 발생했습니다.");
//        }
//    }
}
