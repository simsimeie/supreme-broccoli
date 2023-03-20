package com.search.api.domain.blog.constant;

import lombok.Getter;

@Getter
public enum SearchCriteria {
    A("정확도순"), R("최신순");

    private final String description;

    SearchCriteria(String description) {
        this.description = description;
    }
}
