package com.ktn3.TTMS.service;

import com.ktn3.TTMS.dto.response.ResCommonApi;
import com.ktn3.TTMS.entity.User;

import java.time.LocalDate;

public interface ScheduleService {
    ResCommonApi<?> getMyCalendarTasks(User user, LocalDate from, LocalDate to);
}
