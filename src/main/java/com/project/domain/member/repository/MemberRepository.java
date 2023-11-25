package com.project.domain.member.repository;

import com.project.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Long countByEmail(String email);

    Optional<Member> findMemberByEmail(String email);
}
