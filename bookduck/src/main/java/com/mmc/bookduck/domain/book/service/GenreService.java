package com.mmc.bookduck.domain.book.service;

import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // category랑 GenreName 매핑정보
    private static final Map<String, GenreName> genreMap = new HashMap<>() {{
        put("JUVENILE", GenreName.YOUTH);
        put("YOUNG ADULT", GenreName.YOUTH);
        put("POETRY", GenreName.LITERARY);
        put("BUSINESS", GenreName.BUSINESS);
        put("SELF-HELP", GenreName.SELF_HELP);
        put("TRAVEL", GenreName.TRAVEL);
        put("HISTORY", GenreName.HISTORY);
        put("COMPUTER", GenreName.COMPUTER);
        put("LANGUAGE", GenreName.LANGUAGE);
        put("SOCIAL SCIENCE", GenreName.SOCIETY);
        put("POLITICAL", GenreName.SOCIETY);
        put("LAW", GenreName.SOCIETY);
        put("MATH", GenreName.SCIENCE);
        put("NATURE", GenreName.SCIENCE);
        put("SCIENCE", GenreName.SCIENCE);
        put("BIBLE", GenreName.RELIGION);
        put("RELIGION", GenreName.RELIGION);
        put("COMICS", GenreName.COMICS);
        put("COOKING", GenreName.HOME_COOKING);
        put("FAMILY", GenreName.HOME_COOKING);
        put("HOUSE", GenreName.HOME_COOKING);
        put("HOME", GenreName.HOME_COOKING);
        put("BODY", GenreName.HEALTH);
        put("MIND", GenreName.HEALTH);
        put("SPORT", GenreName.HEALTH);
        put("HEALTH", GenreName.HEALTH);
        put("ARCHITECTURE", GenreName.ARCHITECTURE);
        put("REFERENCE", GenreName.REFERENCE);
        put("STUDY", GenreName.REFERENCE);
        put("HOBBIES", GenreName.HOBBY);
        put("GAME", GenreName.HOBBY);
        put("GARDEN", GenreName.HOBBY);
        put("PET", GenreName.HOBBY);
        put("ART", GenreName.ART);
        put("MUSIC", GenreName.ART);
        put("DESIGN", GenreName.ART);
        put("PHOTO", GenreName.ART);
        put("DRAMA", GenreName.ART);
        put("MEDICAL", GenreName.TECHNOLOGY);
        put("TECHNOLOGY", GenreName.TECHNOLOGY);
        put("TRANSPORTATION", GenreName.TECHNOLOGY);
        put("CRITICISM", GenreName.HUMANITIES);
        put("PHILOSOPHY", GenreName.HUMANITIES);
        put("PSYCHOLOGY", GenreName.HUMANITIES);
        put("EDUCATION", GenreName.HUMANITIES);
        put("FICTION", GenreName.FICTION);
    }};


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

    @Transactional(readOnly = true)
    public Genre findGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(()-> new CustomException(ErrorCode.GENRE_NOT_FOUND));
    }
}