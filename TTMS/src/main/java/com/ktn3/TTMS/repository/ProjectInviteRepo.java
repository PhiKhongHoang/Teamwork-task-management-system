package com.ktn3.TTMS.repository;

import com.ktn3.TTMS.entity.ProjectInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectInviteRepo extends JpaRepository<ProjectInvite, Long> {
    Optional<ProjectInvite> findByToken(String token);
    boolean existsByProjectIdAndEmail(Long projectId, String email);

    Optional<ProjectInvite> findByProjectIdAndEmail(Long projectId, String inviteEmail);
}
