package com.schedule.repository;

import com.schedule.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);
	boolean existsByEmailEquals(String email);
}
