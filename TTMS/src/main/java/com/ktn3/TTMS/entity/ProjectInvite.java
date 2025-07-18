package com.ktn3.TTMS.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_invites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"project_id", "email"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, length = 100)
    private String token; // UUID

    @Column(nullable = false, length = 20)
    private String status; // PENDING, ACCEPTED, DECLINED

    @Column(nullable = false)
    private LocalDateTime invitedAt;

    private LocalDateTime acceptedAt;

    // **Thêm dòng này vào!**
    private LocalDateTime expiredAt;
}

