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
public class FriendResponseDto {
   private Integer friendshipId;
   private Integer friendUserId;
   private String friendNickname;

   public static FriendResponseDto createFriendResponseDto(Friend createdFriend) {
        return new FriendResponseDto(
               createdFriend.getFriendshipId(),
               createdFriend.getFriendUser().getUserId(),
               createdFriend.getFriendUser().getNickname()
       );
   }

}
