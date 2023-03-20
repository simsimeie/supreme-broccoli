package com.search.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularResDto {
    private String query;
    private Long count;

    public PopularResDto(String query, Long count) {
        this.query = query;
        this.count = count;
    }
}
