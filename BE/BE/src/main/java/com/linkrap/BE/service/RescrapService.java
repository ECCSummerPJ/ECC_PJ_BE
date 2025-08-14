package com.linkrap.BE.service;

import com.linkrap.BE.dto.RescrapCreateRequestDto;
import com.linkrap.BE.dto.RescrapCreateResponseDto;
import com.linkrap.BE.dto.RescrapShowResponseDto;
import com.linkrap.BE.dto.ScrapShowResponseDto;
import com.linkrap.BE.entity.Rescrap;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.repository.RescrapRepository;
import com.linkrap.BE.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RescrapService {
    @Autowired
    private final ScrapRepository scrapRepository;
    @Autowired
    private final RescrapRepository rescrapRepository;

    public RescrapCreateResponseDto create(Integer scrapId, RescrapCreateRequestDto dto) {
        //1. 동일한 scrapId 가진 scrap 찾아옴
        Scrap scrap=scrapRepository.findById(scrapId).orElse(null);
        //2. dto->엔티티 변환
        Rescrap rescrap=dto.toEntity(scrap);
        if(rescrap.getRescrapId()!=null)
            return null;
        //rescrap DB에 저장
        Rescrap saved=rescrapRepository.save(rescrap);

        return new RescrapCreateResponseDto(saved.getScrapIdValue(),saved.getRescrapId(),saved.getRedirectLink(),saved.getCreatedAt());
    }

    public RescrapShowResponseDto show(Integer rescrapId) {
        Rescrap rescrap = rescrapRepository.findById(rescrapId).orElseThrow(()->new NoSuchElementException("RESCRAP_NOT_FOUND: "+rescrapId));
        return new RescrapShowResponseDto(rescrap.getRedirectLink());
    }

    public Rescrap delete(Integer rescrapId) {
        Rescrap target=rescrapRepository.findById(rescrapId).orElse(null);
        if(target==null)
            return null;

        rescrapRepository.delete(target);
        return target;
    }
}
