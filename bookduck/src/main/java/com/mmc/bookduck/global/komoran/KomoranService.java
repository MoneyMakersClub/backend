package com.mmc.bookduck.global.komoran;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KomoranService {

    private final Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

    public List<String> extractNounsAndAdjectives(String strToAnalyze) {
        KomoranResult result = komoran.analyze(strToAnalyze);

        // 명사와 형용사만 추출
        return result.getTokenList().stream()
                .filter(token -> "NNG".equals(token.getPos()) || "NNP".equals(token.getPos()) || "VA".equals(token.getPos()))
                .map(Token::getMorph)
                .collect(Collectors.toList());
    }
}