package com.mmc.bookduck.global.komoran;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KomoranService {

    private final Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

    private final Set<String> tooCommonWords = new HashSet<>(Set.of(
            "책", "예", "추천", "생각", "주제", "이야기", "결말", "작가", "캐릭터", "인물", "설정", "전개", "문장", "엔딩",
            "예상", "오늘", "주인공", "작품", "나", "너"
    ));

    public List<String> extractNounsAndAdjectives(String strToAnalyze) {
        KomoranResult result = komoran.analyze(strToAnalyze);

        // 필터링할 품사 목록 - 일반명사, 형용사, 형용사파생형태
        List<String> validPosList = List.of("NNG", "VA", "XR");

        // 명사와 형용사만 추출
        return result.getTokenList().stream()
                .filter(token -> validPosList.contains(token.getPos()))
                .map(Token::getMorph)
                .filter(keyword -> !tooCommonWords.contains(keyword))
                .filter(keyword -> keyword.length() <= 5) // 5글자 이하만 필터링
                .collect(Collectors.toList());
    }
}