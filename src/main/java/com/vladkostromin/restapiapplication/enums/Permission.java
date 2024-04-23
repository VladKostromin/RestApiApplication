package com.vladkostromin.restapiapplication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_UPLOAD("admin:upload"),
    ADMIN_DOWNLOAD("admin:download"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_UPDATE("admin:update"),

    MODERATOR_UPLOAD("moderator:upload"),
    MODERATOR_DOWNLOAD("moderator:download"),
    MODERATOR_DELETE("moderator:delete"),
    MODERATOR_UPDATE("moderator:update"),

    USER_UPLOAD("user:upload"),
    USER_DOWNLOAD("user:download"),
    ;
    private final String permission;
}
