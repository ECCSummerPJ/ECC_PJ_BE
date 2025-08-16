package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
//<<<<<<< HEAD
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Integer> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
    Optional<Users> findByLoginId(String loginId);




//=======
//import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.Optional;

//public interface UsersRepository extends JpaRepository<Users, Integer> {
    // ID, 이메일 중복 체크용
  //  boolean existsByUserId(Integer loginId);
    //boolean existsByEmail(String email);
    //Optional<Users> findByUserId(Integer loginId);

    // 닉네임 중복 확인
   // boolean existsByNickname(String nickname);
//>>>>>>> bbc6cdadad5b3dc8ab8d970fe583fb67aba7c4e0
}
