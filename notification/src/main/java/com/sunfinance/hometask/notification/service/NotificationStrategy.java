package com.sunfinance.hometask.notification.service;

import com.sunfinance.hometask.notification.aggregate.Notification;

import java.util.Optional;

public interface NotificationStrategy {

    Optional<Notification> send(Notification notification);

}
