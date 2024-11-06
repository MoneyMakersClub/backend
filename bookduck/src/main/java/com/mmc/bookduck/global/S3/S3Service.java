package com.mmc.bookduck.global.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import java.io.InputStream;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    private AmazonS3 s3Client;

    // 파일 업로드해서 imgUrl 반환
    public String uploadFile(MultipartFile file) {
        try{
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAIL_TO_S3);
        }

    }

    public void deleteFile(String imgUrl) {
        try {
            String bucketUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/";
            String fileName = imgUrl.substring(bucketUrl.length());

            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));

        } catch (Exception e){
            // 파일 삭제 실패 시 에러를 로깅만
            System.err.println("Failed to delete file: " + imgUrl);
            e.printStackTrace(); // 로그를 남기기 위해 스택 트레이스를 출력
        }
    }
}
