package com.guithub.logging;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggingModel {
    @Autowired
    private static Logger logger = Logger.getLogger(UserApi.class);

    public void makeLog(String massage) {
        logger.error(massage);
    }
}