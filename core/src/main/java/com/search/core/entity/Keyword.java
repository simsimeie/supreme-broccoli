package com.search.core.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(indexes = @Index(name="idx_keyword_01", columnList = "query"))
public class Keyword extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String query;
    private String provider;

    // ********** 생성함수 **********
    public static Keyword createKeyword(String query, String provider){
        Keyword keyword = new Keyword();
        keyword.setQuery(query);
        keyword.setProvider(provider);
        return keyword;
    }


}
