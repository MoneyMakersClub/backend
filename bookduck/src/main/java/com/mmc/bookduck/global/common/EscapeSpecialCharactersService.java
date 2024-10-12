package com.mmc.bookduck.global.common;

import org.springframework.stereotype.Service;

@Service
public class EscapeSpecialCharactersService {

    public static String escapeSpecialCharacters(String keyword) {
        if (keyword != null) {
            // 역슬래시(\)를 먼저 이스케이프 처리
            keyword = keyword.replace("\\", "\\\\");
            // %와 _를 이스케이프 처리
            keyword = keyword.replace("%", "\\%")
                    .replace("_", "\\_");
        }
        return keyword;
    }

}