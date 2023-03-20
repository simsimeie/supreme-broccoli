package com.search.api.domain.keyword.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class KeywordEventPub {
    private String query;
    private String provider;

}
