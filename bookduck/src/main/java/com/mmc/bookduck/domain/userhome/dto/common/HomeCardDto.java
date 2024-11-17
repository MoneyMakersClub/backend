package com.mmc.bookduck.domain.userhome.dto.common;

public sealed interface HomeCardDto permits ExcerptCardDto, OneLineCardDto, BookWithMemoCardDto, BookWithSongCardDto {}

