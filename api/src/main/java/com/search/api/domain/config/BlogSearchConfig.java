package com.search.api.domain.config;

import com.search.api.domain.blog.service.BlogSearchApiImpl;
import com.search.api.domain.blog.service.BlogSearchProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
public class BlogSearchConfig {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 5);
        executor.setThreadNamePrefix("async-event");
        executor.initialize();


        eventMulticaster.setTaskExecutor(executor);
        return eventMulticaster;
    }


    @Bean
    public BlogSearchApiImpl naverBlogSearchApi(
            @Qualifier("naverProvider") BlogSearchProvider naverProvider
            ,ApplicationEventPublisher publisher
    ){
        BlogSearchApiImpl naverBlogSearchApi = new BlogSearchApiImpl(
                naverProvider
                ,null
                ,publisher
        );

        return naverBlogSearchApi;
    }

    @Bean
    public BlogSearchApiImpl kakaoBlogSearchApi(
            @Qualifier("kakaoProvider") BlogSearchProvider kakaoProvider
            ,@Qualifier("naverProvider") BlogSearchProvider naverProvider
            ,ApplicationEventPublisher publisher
    ){
        BlogSearchApiImpl kakaoBlogSearchApi = new BlogSearchApiImpl(
                kakaoProvider
                , naverBlogSearchApi(naverProvider, publisher)
                , publisher);

        return kakaoBlogSearchApi;
    }
}
