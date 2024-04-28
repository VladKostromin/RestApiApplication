package com.vladkostromin.restapiapplication.entity;

import com.vladkostromin.restapiapplication.enums.Role;
import com.vladkostromin.restapiapplication.enums.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
@Table("users")
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity{

    @Column("username")
    private String username;
    @Column("password")
    private String password;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("email")
    private String email;
    @Column("status")
    private UserStatus status;
    @Column("role")
    private Role role;
    @Transient
    private List<EventEntity> events;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

}
