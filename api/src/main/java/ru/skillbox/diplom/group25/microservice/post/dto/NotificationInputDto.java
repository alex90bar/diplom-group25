package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationInputDto {
    private Long authorId;
    private Long userId;
    private NotificationType notificationType;
    private String content;
}
