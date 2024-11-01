package com.mmc.bookduck.domain.excerpt.service;

import lombok.extern.slf4j.Slf4j;
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
        String filePath = googleCloudUploadService.upload(image);
        return extractTextFromImage(filePath);
    }

    private String extractTextFromImage(String filePath) throws IOException {
        // filePath(http://)를 GCS 경로 형식(gs://)으로 변환
        String gcsPath = filePath.split("\\?")[0].replace("https://storage.googleapis.com/download/storage/v1/b/", "gs://")
                .replace("/o/", "/");

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
                if (!res.getTextAnnotationsList().isEmpty()) {
                    return res.getTextAnnotationsList().get(0).getDescription().trim();
                }
            }
            return extractedText.toString().trim();
        }
    }
}
