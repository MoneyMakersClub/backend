package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;


    public Genre matchGenre(List<String> apiCategory) {
        GenreName genreName = categoryToGenreName(apiCategory);
        // 찾은 또는 새로 만든 장르 반환
        return findOrCreateGenreByGenreName(genreName);
    }

    public Genre findOrCreateGenreByGenreName(GenreName genreName){
        // 해당 카테고리와 매칭되는 장르 찾기
        Genre genre = genreRepository.findByGenreName(genreName)
                .orElseGet(() -> {
                    // 장르가 없으면 새로 생성
                    Genre newGenre = new Genre(genreName);
                    return genreRepository.save(newGenre);
                });
        return genre;
    }


    public GenreName categoryToGenreName(List<String> categories){
        // 정보없으면 기타
        if(categories == null || categories.isEmpty()){
            return GenreName.OTHERS;
        }

        String firstCategory = categories.get(0);
        String category = firstCategory.contains("/")
                ? firstCategory.substring(0, firstCategory.indexOf('/')).toUpperCase() : firstCategory.toUpperCase();

        // LITERARY COLLECTION 세부 분류
        if(category.contains("LITERARY COLLECTION")){
            for(String c : categories){
                if(c.toUpperCase().contains("FICTION")){
                    return GenreName.FICTION;
                }
                else if(c.toUpperCase().contains("ESSAY")||c.toUpperCase().contains("POE")){
                    return GenreName.LITERARY;
                }
            }
            return GenreName.LITERARY;
        }

        Map<String, GenreName> genreMap = new HashMap<>();
        // JUVENILE & YOUNG ADULT을 FICTION보다 우선 체크 (JUVENILE FICTION이 YOUTH로 매핑)
        genreMap.put("JUVENILE", GenreName.YOUTH);
        genreMap.put("YOUNG ADULT", GenreName.YOUTH);
        genreMap.put("POETRY", GenreName.LITERARY);
        genreMap.put("BUSINESS", GenreName.BUSINESS);
        genreMap.put("SELF-HELP", GenreName.SELF_HELP);
        genreMap.put("TRAVEL", GenreName.TRAVEL);
        genreMap.put("HISTORY", GenreName.HISTORY);
        genreMap.put("COMPUTER", GenreName.COMPUTER);
        // LANGUAGE를 ART보다 먼저 체크(LANGUAGE ARTS가 LANGUAGE로 매핑)
        genreMap.put("LANGUAGE", GenreName.LANGUAGE);
        // SOCIAL SCIENCE & POLITICAL SCIENCE를 SCIENCE보다 먼저 체크
        genreMap.put("SOCIAL SCIENCE", GenreName.SOCIETY);
        genreMap.put("POLITICAL", GenreName.SOCIETY);
        genreMap.put("LAW", GenreName.SOCIETY);
        genreMap.put("MATH", GenreName.SCIENCE);
        genreMap.put("NATURE", GenreName.SCIENCE);
        genreMap.put("SCIENCE", GenreName.SCIENCE);
        genreMap.put("BIBLE", GenreName.RELIGION);
        genreMap.put("RELIGION", GenreName.RELIGION);
        genreMap.put("COMICS", GenreName.COMICS);
        genreMap.put("COOKING", GenreName.HOME_COOKING);
        genreMap.put("FAMILY", GenreName.HOME_COOKING);
        genreMap.put("HOUSE", GenreName.HOME_COOKING);
        genreMap.put("HOME", GenreName.HOME_COOKING);
        genreMap.put("BODY", GenreName.HEALTH);
        genreMap.put("MIND", GenreName.HEALTH);
        genreMap.put("SPORT", GenreName.HEALTH);
        genreMap.put("HEALTH", GenreName.HEALTH);
        genreMap.put("ARCHITECTURE", GenreName.ARCHITECTURE);
        genreMap.put("REFERENCE", GenreName.REFERENCE);
        genreMap.put("STUDY", GenreName.REFERENCE);
        genreMap.put("HOBBIES", GenreName.HOBBY);
        genreMap.put("GAME", GenreName.HOBBY);
        genreMap.put("GARDEN", GenreName.HOBBY);
        genreMap.put("PET", GenreName.HOBBY);
        genreMap.put("ART", GenreName.ART);
        genreMap.put("MUSIC", GenreName.ART);
        genreMap.put("DESIGN", GenreName.ART);
        genreMap.put("PHOTO", GenreName.ART);
        genreMap.put("DRAMA", GenreName.ART);
        genreMap.put("MEDICAL", GenreName.TECHNOLOGY);
        genreMap.put("TECHNOLOGY", GenreName.TECHNOLOGY);
        genreMap.put("TRANSPORTATION", GenreName.TECHNOLOGY);
        genreMap.put("CRITICISM", GenreName.HUMANITIES);
        genreMap.put("PHILOSOPHY", GenreName.HUMANITIES);
        genreMap.put("PSYCHOLOGY", GenreName.HUMANITIES);
        genreMap.put("EDUCATION", GenreName.HUMANITIES);
        genreMap.put("FICTION", GenreName.FICTION);

        String upperC = category.toUpperCase();
        for (Map.Entry<String, GenreName> entry : genreMap.entrySet()) {
            if (upperC.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return GenreName.OTHERS;
    }

    public String genreNameToKorean(Genre genre){
        return switch (genre.getGenreName()) {
            case FICTION -> "소설";
            case LITERARY -> "시/에세이";
            case BUSINESS -> "경제/경영";
            case SELF_HELP -> "자기계발";
            case SCIENCE -> "과학";
            case SOCIETY -> "정치/사회";
            case LANGUAGE -> "언어";
            case TRAVEL -> "여행";
            case HISTORY -> "역사";
            case COMPUTER -> "컴퓨터/IT";
            case HEALTH -> "건강/스포츠";
            case HOBBY -> "취미/실용";
            case RELIGION -> "종교";
            case ART -> "예술";
            case COMICS -> "만화";
            case HOME_COOKING -> "가정/요리";
            case ARCHITECTURE -> "건축";
            case REFERENCE -> "교재/참고서";
            case TECHNOLOGY -> "기술/공학";
            case HUMANITIES -> "인문";
            case YOUTH -> "아동/청소년";
            case OTHERS -> "기타";
            default -> throw new CustomException(ErrorCode.GENRE_NOT_FOUND);
        };
    }

    public Genre findGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(()-> new CustomException(ErrorCode.GENRE_NOT_FOUND));
    }
}