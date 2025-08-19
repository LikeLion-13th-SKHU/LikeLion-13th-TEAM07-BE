package com.example.ie_um.resource.api;

import com.example.ie_um.global.template.RspTemplate;
import com.example.ie_um.resource.api.dto.request.HashTagReqDto;
import com.example.ie_um.resource.api.dto.response.ResourceListResDto;
import com.example.ie_um.resource.application.ResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
public class ResourcesController {
    private final ResourceService resourceService;

    @PostMapping()
    public Mono<RspTemplate<ResourceListResDto>> forwardHashtags(@Parameter(hidden = true) @AuthenticatedId Long memberId,
                                                                 @RequestBody HashTagReqDto reqDto) {
        return resourceService.forwardHashtags(reqDto)
                .map(data -> new RspTemplate<>(
                        HttpStatus.OK,
                        "해시태그를 전달하여 성공적으로 자원을 추천받았습니다.",
                        data
                ));
    }
}
