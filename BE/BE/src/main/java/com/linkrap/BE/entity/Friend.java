package com.linkrap.BE.entity;



import com.linkrap.BE.dto.FriendRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer friendshipId;
   @Column
   private Integer userId;
   @ManyToOne
   @JoinColumn(name = "friend_user_id")
   private Users friendUser;

   public Friend(Integer userId, Users friendUser) {
       this.userId = userId;
       this.friendUser = friendUser;
   }

   public static Friend createFriend(Integer userId, Users friendUser) {
       return new Friend(userId, friendUser);
   }

    @Column(nullable = false)
    private boolean active = true;

}
