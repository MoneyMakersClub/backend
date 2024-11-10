package com.mmc.bookduck.global.google;

import autovalue.shaded.com.google.common.collect.Lists;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;


@Service
@RequiredArgsConstructor
public class GoogleCloudUploadService {

    @Value("${cloud.gcp.storage.bucket.name}")
    private String bucketName;

    private Storage storage;

    public String upload(MultipartFile file) {
        try {
            // 파일 이름에 UUID를 추가 (중복방지), 한글 및 특수문자 제거
            String sanitizedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "");
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, sanitizedFileName).build(),
                    file.getBytes()
            );
            return blobInfo.getMediaLink();  // 업로드된 파일의 URL 반환
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAIL_TO_GOOGLE);
        }
    }
}
