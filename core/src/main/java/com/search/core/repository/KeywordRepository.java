package com.search.core.repository;


import com.search.core.dto.PopularResDto;
import com.search.core.entity.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("select new com.search.core.dto.PopularResDto(k.query, count(k.query))" +
            " from Keyword k" +
            " group by k.query" +
            " order by count(k.query) desc")
    Page<PopularResDto> popular(Pageable pageable);

}
