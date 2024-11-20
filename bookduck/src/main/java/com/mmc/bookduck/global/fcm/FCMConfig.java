package com.mmc.bookduck.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FCMConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("bookduck-firebase-adminsdk.json");
        if (inputStream == null) {
            throw new CustomException(ErrorCode.FIREBASE_SDK_ERROR);
        }
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();
        return FirebaseApp.initializeApp(options);
    }
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp){
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}