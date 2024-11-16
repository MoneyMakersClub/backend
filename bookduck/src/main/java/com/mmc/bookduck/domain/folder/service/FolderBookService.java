package com.mmc.bookduck.domain.folder.service;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.folder.dto.common.FolderBookOrderUnitDto;
import com.mmc.bookduck.domain.folder.dto.request.FolderBookOrderRequestDto;
import com.mmc.bookduck.domain.folder.entity.Folder;
import com.mmc.bookduck.domain.folder.entity.FolderBook;
import com.mmc.bookduck.domain.folder.repository.FolderBookRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderBookService {
    private final FolderBookRepository folderBookRepository;

    // folderBook 생성
    public List<FolderBook> createFolderBooks(List<UserBook> userBooks, Folder folder){
        List<FolderBook> folderBookList = new ArrayList<>();

        int i = 1;
        for(UserBook userBook : userBooks){
            FolderBook folderBook = new FolderBook(userBook, folder, i);
            i++;

            folderBookList.add(folderBookRepository.save(folderBook));
        }
        return folderBookList;
    }

    // 폴더삭제 시 folderBook 모두 삭제
    public void deleteFolderBookList(Folder folder){
        List<FolderBook> folderBookList = folder.getFolderBooks();
        folderBookRepository.deleteAll(folderBookList);
    }

    // 폴더에서 책 삭제
    public void deleteOneFolderBook(FolderBook folderBook){
        folderBookRepository.delete(folderBook);
    }

    public boolean existsByUserBookAndFolder(UserBook userBook, Folder folder) {
        return folderBookRepository.existsByUserBookAndFolder(userBook,folder);
    }

    public FolderBook findFolderBookById(Long folderBookId) {
        FolderBook folderBook = folderBookRepository.findById(folderBookId)
                .orElseThrow(()->new CustomException(ErrorCode.FOLDERBOOK_NOT_FOUND));
        return folderBook;
    }

    @Transactional
    public void incrementOrderFolderBooks(Folder folder, int size){
        List<FolderBook> existingBooks = folder.getFolderBooks();
        existingBooks.forEach(fb -> fb.setBookOrder(fb.getBookOrder()+size));
    }

    @Transactional
    public void decrementOrderFolderBooks(Folder folder, int deletedBookOrder) {
        List<FolderBook> booksToReorder = folder.getFolderBooks().stream()
                .filter(book -> book.getBookOrder() > deletedBookOrder)
                .collect(Collectors.toList());

        booksToReorder.forEach(book -> book.setBookOrder(book.getBookOrder() - 1));
    }

    @Transactional
    public void updateFolderBookOrder(Folder folder, FolderBookOrderRequestDto dto){
        List<FolderBook> folderBooks = folder.getFolderBooks();

        Map<Long, Integer> newOrderMap = dto.folderBooksOrder().stream()
                .collect(Collectors.toMap(FolderBookOrderUnitDto::folderBookId, FolderBookOrderUnitDto::order));

        for(FolderBook folderBook : folderBooks){
            Integer newOrder = newOrderMap.get(folderBook.getFolderBookId());
            if(newOrder != null){
                folderBook.setBookOrder(newOrder);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<FolderBook> orderFolderBooks(Folder folder){
        return folderBookRepository.findByFolderOrderByBookOrderAsc(folder);
    }
}

