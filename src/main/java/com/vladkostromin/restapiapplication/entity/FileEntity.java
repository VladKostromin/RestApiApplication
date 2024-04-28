package com.vladkostromin.restapiapplication.entity;

import com.vladkostromin.restapiapplication.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("files")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseEntity {

    @Column("file_name")
    private String fileName;
    @Column("location")
    private String location;
    @Column("status")
    private FileStatus status;

}
