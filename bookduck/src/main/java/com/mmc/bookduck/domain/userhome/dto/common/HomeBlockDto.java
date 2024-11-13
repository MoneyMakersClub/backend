package com.mmc.bookduck.domain.userhome.dto.common;

public sealed interface HomeBlockDto permits ExcerptBlockDto, OneLineBlockDto, BookWithMemoBlockDto, BookWithSongBlockDto {}

