package com.example.ie_um.resource.api;

import com.example.ie_um.global.template.RspTemplate;
import com.example.ie_um.resource.api.dto.request.HashTagReqDto;
import com.example.ie_um.resource.api.dto.response.ResourceListResDto;
import com.example.ie_um.resource.application.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
public class ResourcesController {
    private final ResourceService resourceService;

    @PostMapping()
    public RspTemplate<ResourceListResDto> forwardHashtags(@RequestBody HashTagReqDto reqDto) {

        return new RspTemplate<>(
                HttpStatus.OK,
                "해시태그를 성공적으로 전달하였습니다.",
                resourceService.forwardHashtags(reqDto)
        );
    }
}
