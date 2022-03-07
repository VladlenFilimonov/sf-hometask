package com.sunfinance.hometask.notification.service.impl;

import com.sunfinance.hometask.api.event.Topic;
import com.sunfinance.hometask.api.event.notification.NotificationCreatedEvent;
import com.sunfinance.hometask.api.event.notification.NotificationDispatchedEvent;
import com.sunfinance.hometask.api.event.notification.NotificationResult;
import com.sunfinance.hometask.api.event.verification.VerificationCreatedEvent;
import com.sunfinance.hometask.event.EventService;
import com.sunfinance.hometask.notification.aggregate.Notification;
import com.sunfinance.hometask.notification.aggregate.NotificationFactory;
import com.sunfinance.hometask.notification.out.template.TemplateOutService;
import com.sunfinance.hometask.notification.persistance.NotificationEntity;
import com.sunfinance.hometask.notification.persistance.NotificationRepository;
import com.sunfinance.hometask.notification.service.NotificationService;
import com.sunfinance.hometask.notification.service.NotificationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final Map<String, NotificationStrategy> notificationServices;
    private final TemplateOutService templateService;
    private final NotificationFactory factory;
    private final NotificationRepository repository;
    private final EventService eventService;

    @Override
    @Transactional //TODO decompose class, exclude the template receiving flow from transaction
    public Optional<NotificationResult> send(VerificationCreatedEvent event) {
        return Optional.of(event)
                       .map(factory::create)
                       .flatMap(notification -> populateBody(notification, event))
                       .flatMap(this::persistNotification)
                       .flatMap(this::sendNotificationCreatedEvent)
                       .flatMap(notification -> dispatch(notification, event))
                       .flatMap(this::sendNotificationDispatchedEvent)
                       .map(this::toResult);
    }

    private Optional<Notification> sendNotificationDispatchedEvent(Notification notification) {
        return Optional.of(toNotificationDispatchedEvent(notification))
                       .map(event -> {
                           eventService.send(event, Topic.NOTIFICATION_DISPATCHED);
                           return notification;
                       });
    }

    private Optional<Notification> populateBody(Notification notification, VerificationCreatedEvent event) {
        return templateService.getBody(event)
                              .map(body -> factory.setBody(notification, body));
    }

    private Optional<Notification> persistNotification(Notification notification) {
        return Optional.of(toEntity(notification))
                       .map(repository::save)
                       .map(entity -> notification);
    }

    private Optional<Notification> sendNotificationCreatedEvent(Notification notification) {
        return Optional.of(toNotificationCreatedEvent(notification))
                       .map(event -> {
                           eventService.send(event, Topic.NOTIFICATION_CREATED);
                           return notification;
                       });
    }

    private Optional<Notification> dispatch(Notification notification, VerificationCreatedEvent event) {
        return Optional.ofNullable(notificationServices.get(event.getSubject().getType().getVal()))
                       .flatMap(service -> service.send(notification))
                       .flatMap(this::persistDispatchedNotification)
                       .or(() -> throwServiceNotFoundException(event));
    }

    private Optional<Notification> persistDispatchedNotification(Notification notification) {
        return repository.findByNotificationId(notification.getId().toString())
                         .map(entity -> {
                             entity.setDispatched(notification.isDispatched());
                             return entity;
                         })
                         .map(repository::save)
                         .map(entity -> notification);
    }

    private NotificationEntity toEntity(Notification aggregate) {
        return NotificationEntity.builder()
                                 .notificationId(aggregate.getId().toString())
                                 .recipient(aggregate.getRecipient())
                                 .dispatched(aggregate.isDispatched())
                                 .build();
    }

    private NotificationCreatedEvent toNotificationCreatedEvent(Notification notification) {
        return NotificationCreatedEvent.builder()
                                       .id(notification.getId())
                                       .recipient(notification.getRecipient())
                                       .build();
    }

    private NotificationDispatchedEvent toNotificationDispatchedEvent(Notification notification) {
        return NotificationDispatchedEvent.builder()
                                          .id(notification.getId())
                                          .recipient(notification.getRecipient())
                                          .build();
    }

    private NotificationResult toResult(Notification notification) {
        return NotificationResult.builder()
                                 .id(notification.getId())
                                 .build();
    }

    private Optional<Notification> throwServiceNotFoundException(VerificationCreatedEvent event) {
        throw new RuntimeException("No service found for subject type=" + event.getSubject().getType().getVal());
    }
}
