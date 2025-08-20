package com.example.ie_um.global.gcs.application;

import com.example.ie_um.member.domain.Member;
import com.example.ie_um.member.exception.MemberNotFoundException;
import com.example.ie_um.member.repository.MemberRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCSService {
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket.name}")
    private String bucketName;

    public String uploadFile(Long id, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getBytes());

        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }

    public void deleteFile(Long id, String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new RuntimeException("파일 삭제 실패: " + fileName);
        }
    }
}
