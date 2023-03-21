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
    /*
    // 맥락
    블로그 검색 기능과 검색어 저장 기능을 하나의 request, 하나의 thread에서 처리하면,
    DB에 락 등의 문제가 생겼을 때, 서비스의 지연이 바로 바로 나타날 수 있다.

    // 그래서
    스프링 이벤트를 비동기로 사용하여, 블로그 검색 기능과 인기검색어 제공기능의 결합도를 낮춘다.
    DB에 문제가 생기더라도, 당장에 블로그 검색 기능에 문제가 발현되지는 않는다.

    // 개선할 점
    스레드 풀의 스레드 수, 최대 스레드 수, 작업 대기열은 서버 리소스에 따라 최적화할 필요가 있다.
    블로그 검색 기능 어플리케이션과 인기검색어 제공 어플리케이션을 나누고 메시지 브로커를 활용하여 결합도를 낮춘다.
    인기검색어 제공 어플리케이션을 예기치 않게 죽더라도, 메시지 브로커 큐잉을 통해 신뢰성 있는 데이터 프로세스 구축
    */
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
