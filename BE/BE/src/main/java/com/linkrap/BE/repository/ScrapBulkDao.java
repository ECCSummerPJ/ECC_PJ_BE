package com.linkrap.BE.repository.bulk;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ScrapBulkDao {

    @PersistenceContext
    private final EntityManager em;


    @Modifying
    @Transactional
    public int deleteAllByUserId(Integer userId) {
        return em.createQuery("""
            delete from Scrap s
             where s.user.userId = :uid
        """)
                .setParameter("uid", userId)
                .executeUpdate();
    }

    @Modifying
    @Transactional
    public int makePrivateByUserId(Integer userId) {
        return em.createQuery("""
            update Scrap s
               set s.showPublic = false
             where s.user.userId = :uid
        """)
                .setParameter("uid", userId)
                .executeUpdate();
    }
}
