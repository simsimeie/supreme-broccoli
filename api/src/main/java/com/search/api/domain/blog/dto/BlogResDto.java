package com.search.api.domain.blog.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class BlogResDto {
    @JsonAlias({"documents", "items"})
    private List<Details> documents;
    @Getter
    @Setter
    public static class Details{
        private String title;
        @JsonAlias({"contents", "description"})
        private String contents;
        @JsonAlias({"blogname", "bloggername"})
        private String blogname;
        @JsonAlias({"url", "bloggerlink"})
        private String url;
        @JsonAlias({"datetime", "postdate"})
        private String datetime;
    }
}
