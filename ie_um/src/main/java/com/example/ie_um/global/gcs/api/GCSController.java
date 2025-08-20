package com.example.ie_um.global.gcs.api;

import com.example.ie_um.global.annotation.AuthenticatedId;
import com.example.ie_um.global.gcs.application.GCSService;
import com.example.ie_um.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/image")
@Tag(name = "GCS", description = "이미지 GCS 관련 API")
public class GCSController {
    private final GCSService gcsService;

    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 업로드합니다."
    )
    @PostMapping()
    public RspTemplate<String> upload(@Parameter(hidden = true) @AuthenticatedId Long id,
                                      @RequestParam("file") MultipartFile file) {
        try {
            String fileName = gcsService.uploadFile(id, file);
            return new RspTemplate<>(HttpStatus.OK,
                    "이미지가 성공적으로 업로드 되었습니다.",
                    fileName);
        } catch (Exception e) {
            return new RspTemplate<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "이미지 업로드를 실패하였습니다.: " + e.getMessage(),
                    null
            );
        }
    }

    @Operation(
            summary = "이미지 삭제",
            description = "이미지를 삭제합니다."
    )
    @DeleteMapping()
    public RspTemplate<String> delete(@Parameter(hidden = true) @AuthenticatedId Long id,
                                      @RequestParam("fileName") String fileName) {
        try {
            gcsService.deleteFile(id, fileName);
            return new RspTemplate<>(HttpStatus.OK,
                    "이미지가 성공적으로 삭제되었습니다.",
                    fileName);
        } catch (Exception e) {
            return new RspTemplate<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "이미지 삭제를 실패하였습니다.: " + e.getMessage(),
                    null
            );
        }
    }
}
