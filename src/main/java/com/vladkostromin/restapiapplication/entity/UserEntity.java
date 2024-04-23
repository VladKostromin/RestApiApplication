package com.vladkostromin.restapiapplication.entity;

import com.vladkostromin.restapiapplication.enums.Role;
import com.vladkostromin.restapiapplication.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
@Table("users")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity{

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Status status;
    private Role role;
    @Transient
    private List<EventEntity> events;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

}
