package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    private Integer friendUserId;
}
