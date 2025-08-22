package com.example.ie_um.resource.api;

import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.template.RspTemplate;
import com.example.ie_um.resource.api.dto.request.HashTagReqDto;
import com.example.ie_um.resource.api.dto.response.ResourceListResDto;
import com.example.ie_um.resource.application.ResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Resource", description = "자원 추천 관련 API")
public class ResourcesController {
    private final ResourceService resourceService;

    @Operation(
            summary = "자원 추천",
            description = "자원을 추천합니다."
    )
    @ApiResponse(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResourceListResDto.class)
            )
    )
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
