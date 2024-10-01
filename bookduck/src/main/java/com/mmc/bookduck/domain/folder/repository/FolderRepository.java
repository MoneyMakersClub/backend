package com.mmc.bookduck.domain.folder.repository;

import com.mmc.bookduck.domain.folder.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
