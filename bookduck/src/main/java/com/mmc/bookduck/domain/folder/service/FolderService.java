package com.mmc.bookduck.domain.folder.service;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.folder.dto.common.CandidateFolderBookDto;
import com.mmc.bookduck.domain.folder.dto.common.FolderBookCoverListDto;
import com.mmc.bookduck.domain.folder.dto.request.FolderBookOrderRequestDto;
import com.mmc.bookduck.domain.folder.dto.request.FolderRequestDto;
import com.mmc.bookduck.domain.folder.dto.response.AllFolderListResponseDto;
import com.mmc.bookduck.domain.folder.dto.response.CandidateFolderBookListResponseDto;
import com.mmc.bookduck.domain.folder.dto.response.FolderBookListResponseDto;
import com.mmc.bookduck.domain.folder.dto.common.FolderBookUnitDto;
import com.mmc.bookduck.domain.folder.dto.response.FolderResponseDto;
import com.mmc.bookduck.domain.folder.entity.Folder;
import com.mmc.bookduck.domain.folder.entity.FolderBook;
import com.mmc.bookduck.domain.folder.repository.FolderRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.Errors;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserBookService userBookService;
    private final FolderBookService folderBookService;
    private final UserService userService;


    // 폴더 생성
    public FolderResponseDto createFolder(FolderRequestDto dto, Errors error) {
        if(error.hasErrors()){
            throw new CustomException(ErrorCode.INVALID_INPUT_LENGTH);
        }

        User user = userService.getCurrentUser();
        if(folderRepository.existsByFolderNameAndUser(dto.folderName(), user)){
            throw new CustomException(ErrorCode.FOLDERNAME_ALREADY_EXISTS);
        }
        Folder folder = new Folder(dto.folderName(), user);

        Folder savedFolder = folderRepository.save(folder);
        return FolderResponseDto.from(savedFolder);
    }

    // 폴더명 수정
    public FolderResponseDto updateFolder(Long folderId, FolderRequestDto dto, Errors error) {
        if(error.hasErrors()){
            throw new CustomException(ErrorCode.INVALID_INPUT_LENGTH);
        }

        User user = userService.getCurrentUser();
        if(folderRepository.existsByFolderNameAndUser(dto.folderName(), user)){
            throw new CustomException(ErrorCode.FOLDERNAME_ALREADY_EXISTS);
        }
        Folder folder = findFolderById(folderId);

        folder.updateFolderName(dto.folderName());
        return FolderResponseDto.from(folder);
    }

    // 폴더 삭제
    public String deleteFolder(Long folderId) {
        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);

        // 권한확인
        if(folder.getUser().equals(user)){
            // 폴더 책들을 다 삭제
            folderBookService.deleteFolderBookList(folder);
            folderRepository.delete(folder);
            return "폴더를 삭제했습니다.";
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }


    // 폴더에 책 추가
    public FolderBookListResponseDto addFolderBooks(Long folderId, List<Long> userBookIds) {

        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);

        List<UserBook> userBookList = new ArrayList<>();
        for (Long userBookId : userBookIds) {
            UserBook userBook = userBookService.getUserBookById(userBookId);
            if (!userBook.getUser().equals(user)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
            }
            if (folderBookService.existsByUserBookAndFolder(userBook, folder)) {
                // 이미 folderBook 있을 때,
                throw new CustomException(ErrorCode.FOLDERBOOK_ALREADY_EXISTS);
            }
            userBookList.add(userBookService.getUserBookById(userBookId));
        }

        List<FolderBookUnitDto> folderBookList = new ArrayList<>();

        folderBookService.incrementOrderFolderBooks(folder, userBookList.size());
        List<FolderBook> newFolderBookList = folderBookService.createFolderBooks(userBookList, folder);
        for(FolderBook folderBook : newFolderBookList){
            folder.addFolderBook(folderBook);
        }
        List<FolderBook> folderBooks = folderBookService.orderFolderBooks(folder);

        for (FolderBook folderBook : folderBooks) {
            folderBookList.add(FolderBookUnitDto.from(folderBook));
        }
        return new FolderBookListResponseDto(folder, folderBookList);
    }

    // 폴더에서 책 삭제
    public FolderBookListResponseDto deleteFolderBooks(Long folderId, List<Long> folderBookIds) {
        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);
        if(!folder.getUser().equals(user)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        List<FolderBook> folderBookList = new ArrayList<>();
        for(Long folderBookId : folderBookIds){
            FolderBook folderBook = folderBookService.findFolderBookById(folderBookId);
            if(!folderBook.getFolder().equals(folder)){
                throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
            }
            folderBookList.add(folderBook);
        }

        for(FolderBook folderBook : folderBookList){
            folder.removeFolderBook(folderBook);
            folderBookService.deleteOneFolderBook(folderBook);
            folderBookService.decrementOrderFolderBooks(folder, folderBook.getBookOrder());
        }

        List<FolderBook> orderedFolderBooks = folderBookService.orderFolderBooks(folder);
        List<FolderBookUnitDto> dtoList = new ArrayList<>();
        for(FolderBook book : orderedFolderBooks){
            dtoList.add(FolderBookUnitDto.from(book));
        }
        return new FolderBookListResponseDto(folder, dtoList);

    }

    // 폴더 별 도서 목록 조회
    @Transactional(readOnly = true)
    public FolderBookListResponseDto getFolderBookList(Long folderId) {
        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);

        List<FolderBookUnitDto> folderBookList = new ArrayList<>();

        if(folder.getUser().equals(user)){
            for(FolderBook folderBook : folder.getFolderBooks()){
                folderBookList.add(FolderBookUnitDto.from(folderBook));
            }
            return new FolderBookListResponseDto(folder, folderBookList);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    // 전체 폴더 목록 조회
    @Transactional(readOnly = true)
    public AllFolderListResponseDto getAllFolderList() {
        User user = userService.getCurrentUser();
        List<Folder> folders = folderRepository.findAllByUserOrderByFolderIdDesc(user);

        List<FolderBookCoverListDto> folderList = new ArrayList<>();

        if(folders != null){
            for(Folder folder : folders){
                folderList.add(FolderBookCoverListDto.from(folder, folder.getFolderBooks()));
            }
        }
        return new AllFolderListResponseDto(folderList);
    }

    @Transactional(readOnly = true)
    public Folder findFolderById(Long folderId){
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(()-> new CustomException(ErrorCode.FOLDER_NOT_FOUND));
        return folder;
    }

    // 폴더별 & 상태별 도서 목록 조회
    @Transactional(readOnly = true)
    public FolderBookListResponseDto getFolderBookListStatus(Long folderId, List<String> statusList) {

        if(statusList.isEmpty()){
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);

        List<ReadStatus> readStatusList = userBookService.validateReadStatus(statusList);
        List<FolderBookUnitDto> folderBookList = new ArrayList<>();
        if(folder.getUser().equals(user)){
            for(FolderBook folderBook : folder.getFolderBooks()){
                for(ReadStatus readStatus : readStatusList){
                    if(folderBook.getUserBook().getReadStatus().equals(readStatus)){
                        folderBookList.add(FolderBookUnitDto.from(folderBook));
                    }
                }
            }
            return new FolderBookListResponseDto(folder, folderBookList);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public CandidateFolderBookListResponseDto getCandidateBooks(Long folderId, List<String> statusList) {
        User user = userService.getCurrentUser();
        Folder folder = findFolderById(folderId);

        if(!(folder.getUser().equals(user))){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        List<FolderBook> folderBooks = folder.getFolderBooks();
        List<UserBook> userBooks = userBookService.findAllByUser(user);

        List<CandidateFolderBookDto> dtoList = new ArrayList<>();
        List<UserBook> candidateList = new ArrayList<>(userBooks);

        for(UserBook userBook: userBooks){
            for(FolderBook folderBook: folderBooks){
                if(userBook.equals(folderBook.getUserBook())){
                    candidateList.remove(userBook);
                    break;
                }
            }
        }

        if(statusList == null || statusList.isEmpty()){
            for(UserBook userBook: candidateList){
                dtoList.add(CandidateFolderBookDto.from(userBook));
            }
        }
        else{
            List<ReadStatus> readStatusList = userBookService.validateReadStatus(statusList);
            for(ReadStatus readStatus : readStatusList){
                for(UserBook userBook: candidateList){
                    if(userBook.getReadStatus().equals(readStatus)){
                        dtoList.add(CandidateFolderBookDto.from(userBook));
                    }
                }
            }
        }
        return new CandidateFolderBookListResponseDto(dtoList);
    }

    public FolderBookListResponseDto updateFolderBookOrder(Long folderId, FolderBookOrderRequestDto requestDto) {
        Folder folder = findFolderById(folderId);
        folderBookService.updateFolderBookOrder(folder, requestDto);

        List<FolderBookUnitDto> folderBookList = new ArrayList<>();
        List<FolderBook> folderBooks = folderBookService.orderFolderBooks(folder);
        for (FolderBook folderBook : folderBooks) {
            folderBookList.add(FolderBookUnitDto.from(folderBook));
        }
        return new FolderBookListResponseDto(folder, folderBookList);
    }
}
