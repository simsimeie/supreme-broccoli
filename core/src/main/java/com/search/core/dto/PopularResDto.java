package com.search.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PopularResDto {
    private String query;
    private Long count;

    public PopularResDto(String query, Long count) {
        this.query = query;
        this.count = count;
    }
}
