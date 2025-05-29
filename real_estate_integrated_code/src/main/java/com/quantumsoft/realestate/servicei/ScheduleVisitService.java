package com.quantumsoft.realestate.servicei;

import com.quantumsoft.realestate.entity.ScheduleVisit;

public interface ScheduleVisitService {
    String scheduleVisit(Long id, ScheduleVisit scheduleVisit, String email);

    String managePropertyVisit(Long id, String visitStatus);
}
