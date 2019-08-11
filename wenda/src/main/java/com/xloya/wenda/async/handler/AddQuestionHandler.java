package com.xloya.wenda.async.handler;

import com.xloya.wenda.async.EventHandler;
import com.xloya.wenda.async.EventModel;
import com.xloya.wenda.async.EventType;
import com.xloya.wenda.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddQuestionHandler.class);
    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel eventModel) {
        try {
            searchService.indexQuestion(eventModel.getEntity_id(),
                    eventModel.getExt("title"), eventModel.getExt("content"));
        } catch (Exception e) {
            LOGGER.error("增加题目索引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        {
            return Arrays.asList(EventType.ADD_QUESTION);
        }
    }
}
