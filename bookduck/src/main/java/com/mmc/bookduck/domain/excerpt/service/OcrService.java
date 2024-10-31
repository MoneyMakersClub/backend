package com.mmc.bookduck.domain.excerpt.service;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.google.GoogleCloudUploadService;
import com.google.cloud.vision.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OcrService {
    private final GoogleCloudUploadService googleCloudUploadService;

    public String processOcr(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_IMAGE_FILE);
        }
        // 이미지 업로드 후 URL 생성
        String filePath = googleCloudUploadService.upload(image);
        return extractTextFromImage(filePath);
    }

    private String extractTextFromImage(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            StringBuilder extractedText = new StringBuilder();
            for (AnnotateImageResponse res : response.getResponsesList()) {
                if (res.hasError()) {
                    throw new CustomException(ErrorCode.EXTERNAL_API_ERROR);
                }
                res.getTextAnnotationsList().forEach(annotation -> extractedText.append(annotation.getDescription()).append("\n"));
            }

            return extractedText.toString().trim();
        }
    }
}
