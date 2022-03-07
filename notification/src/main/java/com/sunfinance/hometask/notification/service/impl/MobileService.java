package com.sunfinance.hometask.notification.service.impl;

import com.sunfinance.hometask.notification.aggregate.Notification;
import com.sunfinance.hometask.notification.service.NotificationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sunfinance.hometask.api.SubjectTypeConstant.MOBILE_CONFIRMATION_NAME;

@Service(MOBILE_CONFIRMATION_NAME)
@AllArgsConstructor
public class MobileService implements NotificationStrategy {

    @Override
    public Optional<Notification> send(Notification notification) {
        //TODO implement
        return Optional.empty();
    }

}
