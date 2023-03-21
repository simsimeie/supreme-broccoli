package com.search.api.domain.blog.constant;

import com.search.core.constant.ErrorCode;
import com.search.core.exception.KBException;
import lombok.Getter;


@Getter
public enum SearchCriteria {
    A("정확도순"), R("최신순");

    private final String description;

    SearchCriteria(String description) {
        this.description = description;
    }


    public static SearchCriteria transform(String criteria) {
        boolean isContain=false;
        SearchCriteria searchCriteria = SearchCriteria.A;

        for(SearchCriteria cri : SearchCriteria.values()){
            if(cri.name().equals(criteria)){
                isContain = true;
                searchCriteria = cri;
                break;
            }
        }

        if(!isContain) throw new KBException(ErrorCode.SEARCH_CRITERIA_VALIDATION_ERROR);

        return searchCriteria;
    }
}
