package com.search.api.domain.blog.dto;

import com.search.api.domain.blog.constant.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class SearchCondDto {
    private String keyword;
    private SearchCriteria criteria;
    private Integer page;
    private Integer size;
}
