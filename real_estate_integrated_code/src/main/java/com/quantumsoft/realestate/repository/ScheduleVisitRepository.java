package com.quantumsoft.realestate.repository;

import com.quantumsoft.realestate.entity.ScheduleVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleVisitRepository extends JpaRepository<ScheduleVisit, Long> {
}
