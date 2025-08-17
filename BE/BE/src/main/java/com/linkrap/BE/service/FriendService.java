package com.linkrap.BE.service;

import com.linkrap.BE.dto.FriendRequestDto;
import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.entity.Friend;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.FriendRepository;
import com.linkrap.BE.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    UsersRepository usersRepository;

    //친구 조회
    public List<FriendResponseDto> friends(Integer userId){
        return friendRepository.findByUserId(userId)
                .stream()
                .map(FriendResponseDto::createFriendResponseDto)
                .collect(Collectors.toList());
    }

    //친구 등록
    public FriendResponseDto create(Integer userId, FriendRequestDto dto) {
        Users friendUser = usersRepository.findById(dto.getFriendUserId()).orElseThrow(()->new IllegalArgumentException("등록된 사용자가 아닙니다."));
        Friend friend = Friend.createFriend(userId, friendUser);
        Friend createdFriend = friendRepository.save(friend);
        return FriendResponseDto.createFriendResponseDto(createdFriend);
    }

    //친구 해제
    public FriendResponseDto delete(Integer friendshipId) {
        Friend target = friendRepository.findByFriendshipId(friendshipId);
        friendRepository.delete(target);
        return FriendResponseDto.createFriendResponseDto(target);
    }

    //친구 관계 확인
    public boolean checkFriendship(Integer userId, Integer friendUserId) {
        return friendRepository.existsByUserIdAndFriendUserId(userId, friendUserId);
    }
}
