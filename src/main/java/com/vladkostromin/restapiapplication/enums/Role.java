package com.vladkostromin.restapiapplication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(Permission.ADMIN_UPLOAD,
            Permission.ADMIN_DOWNLOAD,
            Permission.ADMIN_DELETE,
            Permission.ADMIN_UPDATE)),
    MODERATOR(Set.of(Permission.MODERATOR_UPDATE,
            Permission.MODERATOR_UPLOAD,
            Permission.MODERATOR_DELETE,
            Permission.MODERATOR_DOWNLOAD)),
    USER(Set.of(Permission.USER_UPLOAD,
            Permission.USER_DOWNLOAD))
    ;

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
