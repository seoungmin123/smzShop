package com.server.smzshop.calendar.application;

import com.server.smzshop.calendar.domain.CalendarEvent;
import com.server.smzshop.calendar.domain.CalendarEventRepository;
import com.server.smzshop.calendar.dto.EventRequest;
import com.server.smzshop.calendar.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    private final CalendarEventRepository eventRepository;

    // 기간별 일정 조회
    public List<EventResponse> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<CalendarEvent> events = eventRepository.findEventsByDateRange(startDate, endDate);
        return events.stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }

    // 일정 등록
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        CalendarEvent event = CalendarEvent.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .color(request.getColor() != null ? request.getColor() : "#3788d8")
                .build();

        CalendarEvent saved = eventRepository.save(event);
        return EventResponse.from(saved);
    }

    // 일정 수정
    @Transactional
    public EventResponse updateEvent(Long eventId, EventRequest request) {
        CalendarEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setColor(request.getColor());

        return EventResponse.from(event);
    }

    // 일정 삭제
    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}