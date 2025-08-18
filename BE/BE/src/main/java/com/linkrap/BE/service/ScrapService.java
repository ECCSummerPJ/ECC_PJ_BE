package com.linkrap.BE.service;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.*;
import com.linkrap.BE.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {
    @Autowired
    private final ScrapRepository scrapRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UsersRepository usersRepository;
    @Autowired
    private final ScrapViewRepository scrapViewRepository;
    @Autowired
    private final CommentRepository commentRepository;

    @Transactional
    public ScrapCreateResponseDto create(Integer userId, ScrapCreateRequestDto dto){
        Users user= usersRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 생성자가 없습니다."));
        Category category=categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()->new IllegalArgumentException("스크랩 생성 실패! "+"대상 카테고리가 없습니다."));
        //스크랩 작성 제한
        if (dto.getScrapTitle() == null || dto.getScrapTitle().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapLink() == null || dto.getScrapLink().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapLink().length() > 255) {
            throw new IllegalArgumentException("url은 255자 이하여야 합니다.");
        }
        if (dto.getScrapMemo() == null || dto.getScrapMemo().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapMemo().length() > 500) {
            throw new IllegalArgumentException("메모는 500자 이하여야 합니다.");
        }
        //2. 스크랩 생성
        Scrap scrap=Scrap.createScrap(dto,user,category);
        //3. 스크랩 엔티티를 DB에 저장
        Scrap saved=scrapRepository.save(scrap);
        //4. DTO로 변환 및 반환
        return new ScrapCreateResponseDto(saved.getScrapId(),saved.getUserIdValue(),saved.getCreatedAt());
    }

    public List<ScrapListDto> index() {
        return scrapRepository.findAllScrap();
    }



    public ScrapShowResponseDto show(Integer scrapId){
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));
        List<CommentShowDto> comments = commentRepository.findCommentsByScrapId(scrapId);

        //소유자 id 꺼내기
        Integer ownerId = null;
        if (scrap.getUser() != null) {
            ownerId = scrap.getUser().getUserId();
        }

        //     일단 소유자의 스크랩이 조회되면 소유자 조회수 1 증가로 기록,나중에 로그인 붙이면 requestUserId == ownerId 일 때만 저장하기
        if (ownerId != null) {
            scrapViewRepository.save(
                    ScrapView.builder()
                            .scrapId(scrap)
                            .userId(ownerId)    // 소유자 id 저장
                            .build()
            );
        }

        return new ScrapShowResponseDto(scrap.getScrapId(),scrap.getUserIdValue(),scrap.getCategoryIdValue(),scrap.getScrapTitle(),scrap.getScrapLink(),scrap.getScrapMemo(),scrap.isFavorite(),scrap.isShowPublic(),scrap.getCreatedAt(),scrap.getUpdatedAt(),comments);
    }

    @Transactional
    public ScrapChangeResponseDto update(Integer scrapId, Integer userId, ScrapChangeRequestDto dto){
        //1. 스크랩 조회 및 예외 발생
        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));
        //스크랩 작성 제한
        if (target.getUserIdValue()!=userId) {
            throw new IllegalArgumentException("본인 스크랩만 수정할 수 있습니다.");
        }
        if (dto.getScrapTitle() == null || dto.getScrapTitle().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapLink() == null || dto.getScrapLink().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapLink().length() > 255) {
            throw new IllegalArgumentException("url은 255자 이하여야 합니다.");
        }
        if (dto.getScrapMemo() == null || dto.getScrapMemo().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getScrapMemo().length() > 500) {
            throw new IllegalArgumentException("메모는 500자 이하여야 합니다.");
        }
        //2. 스크랩 수정
        if(dto.getCategoryId()!=null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new NoSuchElementException("CATEGORY_NOT_FOUND: " + dto.getCategoryId()));
            target.patch(dto, category);
        }
        else target.patch(dto,null);
        //3. DB로 갱신
        Scrap updated=scrapRepository.save(target);
        scrapRepository.flush();
        updated=scrapRepository.findById(updated.getScrapId()).orElseThrow();
        //4. 스크랩 엔티티를 DTO로 변환 및 반환
        return new ScrapChangeResponseDto(updated.getUpdatedAt());
    }

    @Transactional
    public ScrapDto delete(Integer scrapId, Integer userId) {
        //1. 스크랩 조회 및 예외 발생
        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));
        if (target.getUserIdValue()!=userId) {
            throw new IllegalArgumentException("본인 스크랩만 삭제할 수 있습니다.");
        }
        //2. 스크랩 삭제
        commentRepository.deleteComment(scrapId);
        scrapRepository.delete(target);
        //3. 삭제 스크랩을 DTO로 변환 및 반환
        return ScrapDto.createScrapDto(target);
    }

    public ScrapFavoriteDto favorite(Integer scrapId) {
        Scrap target=scrapRepository.findById(scrapId).orElseThrow(()->new NoSuchElementException("SCRAP_NOT_FOUND: "+scrapId));
        //ScrapFavoriteDto favoriteDto=ScrapFavoriteDto.createScrapFavoriteDto(target);
        target.patchFavorite(target);
        Scrap favorite=scrapRepository.save(target);
        return new ScrapFavoriteDto(favorite.isFavorite());
    }

    @Transactional
    public List<ScrapListDto> search(String keyword) {
        List<ScrapListDto> scrapDtoList=scrapRepository.findByScrapTitleContaining(keyword);
        if (keyword == null || keyword.isBlank() || keyword.length() < 2) {
            throw new IllegalArgumentException("검색어는 2자 이하여야 합니다.");
        }
        if (keyword.length() > 30) {
            throw new IllegalArgumentException("검색어는 30자 이하여야 합니다.");
        }

        //scrapDtoList.isEmpty() 이면 검색 결과 없음
        return scrapDtoList;
    }

    //전체 스크랩 목록 즐겨찾기 및 공개 여부 필터
    public List<ScrapListDto> getAllScrapsByFilter(Integer userId,
                                                Boolean favorite,
                                                Boolean showPublic) {
        //기본 조회
        Specification<Scrap> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("user").get("userId"), userId)
                );
        //즐겨찾기 필터
        if (favorite != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("favorite"), favorite)
            );
        }
        //공개여부 필터
        if (showPublic != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("showPublic"), showPublic)
            );
        }
        //스크랩 목록 반환
        List<Scrap> filteredScraps = scrapRepository.findAll(spec);
        return filteredScraps.stream()
                .map(ScrapListDto::createScrapListDto)
                .collect(Collectors.toList());
    }

    //카테고리별 즐겨찾기 및 공개 여부 필터
    public List<ScrapListDto> getScrapsByFilter(Integer userId,
                                                Integer categoryId,
                                                Boolean favorite,
                                                Boolean showPublic) {
        //기본 조회
        Specification<Scrap> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("user").get("userId"), userId),
                        criteriaBuilder.equal(root.get("category").get("categoryId"), categoryId)
                );
        //즐겨찾기 필터
        if (favorite != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("favorite"), favorite)
            );
        }
        //공개여부 필터
        if (showPublic != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("showPublic"), showPublic)
            );
        }
        //스크랩 목록 반환
        List<Scrap> filteredScraps = scrapRepository.findAll(spec);
        return filteredScraps.stream()
                .map(ScrapListDto::createScrapListDto)
                .collect(Collectors.toList());
    }

    //친구의 공개 게시글 열람
    public List<ScrapListDto> getPublicScraps(Integer friendUserId, Boolean favorite, Integer categoryId) {
        List<Scrap> friendScraps = scrapRepository.findByUser_UserIdAndShowPublicIsTrue(friendUserId);
        Stream<Scrap> filteredStream = friendScraps.stream();
        //즐겨찾기 필터
        if (Boolean.TRUE.equals(favorite)){
            filteredStream = filteredStream.filter(Scrap::isFavorite);
        }
        //카테고리 필터
        if (categoryId != null){
            filteredStream = filteredStream.filter(scrap -> scrap.getCategory() != null && (categoryId != null && categoryId.equals(scrap.getCategory().getCategoryId())));
        }
        return filteredStream.map(ScrapListDto::createScrapListDto).collect(Collectors.toList());
    }

    //스크랩 열람 여부 기록
    public void markAsRead(Integer scrapId) {
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 스크랩입니다."));
        scrap.setRead(true);
        scrapRepository.save(scrap);
    }

    //리마인드 알람 목록
    public List<RemindDto> getUnreadScrapsByOldest(Integer userId, int i) {
        List<Scrap> unreadScraps = scrapRepository.findTop5ByUser_UserIdAndReadFalseOrderByCreatedAtAsc(userId);
        return unreadScraps.stream()
                .map(RemindDto::createRemindDto)
                .collect(Collectors.toList());

    }
}
