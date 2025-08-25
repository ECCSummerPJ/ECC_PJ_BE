package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Rescrap;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RescrapRepository extends CrudRepository<Rescrap, Integer>  {

    @Modifying
    //scrap 지워지면 해당 rescrap들도 지워지도록 하는 기능
    @Query("""
        delete
        from Rescrap r
        where r.scrap.scrapId=:scrapId
    """)
    void deleteRescrap(@Param("scrapId") Integer scrapId);
}
