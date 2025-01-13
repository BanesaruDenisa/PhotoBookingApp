package com.example.photobooking.services;

import com.example.photobooking.model.entities.User;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;


@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void notifyUser(String message, User user) {
        logger.info("Sending notification to user {} (ID={}): {}", user.getEmail(), user.getId(), message);
    }
}
