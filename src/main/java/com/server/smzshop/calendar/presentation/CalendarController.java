package com.server.smzshop.calendar.presentation;

import com.server.smzshop.calendar.application.CalendarService;
import com.server.smzshop.calendar.dto.EventRequest;
import com.server.smzshop.calendar.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CalendarController {

    private final CalendarService calendarService;

    // 일정 조회
    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        System.out.println("🔍 받은 날짜 범위: " + startDate + " ~ " + endDate); // ← 추가
        List<EventResponse> events = calendarService.getEventsByDateRange(startDate, endDate);
        System.out.println("📊 조회된 일정 수: " + events.size()); // ← 추가

        return ResponseEntity.ok(events);
    }

    // 일정 등록
    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        EventResponse event = calendarService.createEvent(request);
        return ResponseEntity.ok(event);
    }

    // 일정 수정
    @PutMapping("/events/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest request
    ) {
        EventResponse event = calendarService.updateEvent(eventId, request);
        return ResponseEntity.ok(event);
    }

    // 일정 삭제
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        calendarService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }
}