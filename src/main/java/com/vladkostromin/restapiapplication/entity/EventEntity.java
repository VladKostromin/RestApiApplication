package com.vladkostromin.restapiapplication.entity;


import com.vladkostromin.restapiapplication.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("events")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity extends BaseEntity {

    @Transient
    private UserEntity user;
    @Transient
    private FileEntity file;
    @Column("status")
    private EventStatus status;
    @Column("user_id")
    private Long userId;
    @Column("file_id")
    private Long fileId;

}
