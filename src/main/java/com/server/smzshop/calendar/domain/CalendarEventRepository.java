package com.server.smzshop.calendar.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    @Query("SELECT e FROM CalendarEvent e WHERE " +
            "(e.startDate BETWEEN :startDate AND :endDate) OR " +
            "(e.endDate BETWEEN :startDate AND :endDate) OR " +
            "(e.startDate <= :startDate AND e.endDate >= :endDate)")
    List<CalendarEvent> findEventsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}