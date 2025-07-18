package com.ktn3.TTMS.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull @Size(max = 50)
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @NotNull(message = "Tên quyền không được null")
    @NotBlank(message = "Tên quyền không được để trống")
    @Size(max = 100, message = "Tên quyền tối đa 100 ký tự")
    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "Mô tả quyền tối đa 255 ký tự")
    @Column(length = 255)
    private String description;
}

