package com.linkrap.BE.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class FriendBulkDao {

    @PersistenceContext
    private final EntityManager em;

    @Modifying
    @Transactional
    public int deleteAllByUserIdOrFriendId(Integer userId) {
        return em.createQuery("""
            delete from Friend f
            where f.user.userId = :uid or f.friend.userId = :uid
        """)
                .setParameter("uid", userId)
                .executeUpdate();
    }


    @Modifying @Transactional
    public int softDetachAllForUser(Integer userId) {
        return em.createQuery("""
            update Friend f
               set f.active = false
             where f.user.userId = :uid or f.friend.userId = :uid
        """)
                .setParameter("uid", userId)
                .executeUpdate();
    }
}
