package com.search.api.common.config;

import com.search.api.domain.blog.service.BlogSearchApiImpl;
import com.search.api.domain.blog.service.BlogSearchProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class BlogSearchConfig {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        //executor.setMaxPoolSize(10);
        //executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("async-event");
        executor.initialize();


        eventMulticaster.setTaskExecutor(executor);
        return eventMulticaster;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
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
