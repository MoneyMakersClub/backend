package com.mmc.bookduck.domain.homecard.dto.common;

public sealed interface HomeCardDto permits ExcerptCardDto, OneLineCardDto, BookWithMemoCardDto, BookWithSongCardDto {}

