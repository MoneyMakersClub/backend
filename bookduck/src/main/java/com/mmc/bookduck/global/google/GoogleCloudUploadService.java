package com.mmc.bookduck.global.google;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.cloud.storage.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class GoogleCloudUploadService {

    @Value("${cloud.gcp.storage.bucket.name}")
    private String bucketName;

    private static Storage storage = StorageOptions.getDefaultInstance().getService();

    public String upload(MultipartFile file) {
        try {
            // 파일 이름에 UUID를 추가 (중복방지)
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, fileName).build(),
                    file.getBytes()
            );
            return blobInfo.getMediaLink();  // 업로드된 파일의 URL 반환
        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAIL_TO_GOOGLE);
        }
    }
}
