package com.search.api.domain.keyword.event;

import com.search.api.domain.keyword.service.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeywordEventSub {
    private final PopularKeywordService popularKeywordService;
   @EventListener
    public void listen(KeywordEventPub data) {
       log.info("Query : {}, Provider : {}" , data.getQuery(), data.getProvider() );
       popularKeywordService.saveKeywordHistory(data.getQuery(), data.getProvider());
   }
}
