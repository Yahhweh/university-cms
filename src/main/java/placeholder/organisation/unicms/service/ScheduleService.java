package placeholder.organisation.unicms.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.Schedule;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.repository.RoomRepository;
import placeholder.organisation.unicms.repository.ScheduleRepository;
import placeholder.organisation.unicms.service.dto.request.ScheduleRequestDTO;
import placeholder.organisation.unicms.service.mapper.ScheduleMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Log4j2
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;
    private final LessonRepository lessonRepository;
    private final RoomRepository roomRepository;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByGroupId(Long groupId) {
        return scheduleRepository.findByGroupId(groupId);
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Transactional
    public void creatSchedule(ScheduleRequestDTO requestDTO) {
        validateNoLecturerConflict(requestDTO, -1L);
        Schedule schedule = scheduleMapper.toEntity(requestDTO);
        scheduleRepository.save(schedule);
        log.debug("Schedule saved successfully. Day: {}", schedule.getDay());
    }

    @Transactional
    public void updateSchedule(ScheduleRequestDTO requestDTO, Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new EntityNotFoundException(Schedule.class, String.valueOf(scheduleId));
        }
        validateNoLecturerConflict(requestDTO, scheduleId);
        Schedule schedule = scheduleMapper.toEntity(requestDTO);
        schedule.setId(scheduleId);
        scheduleRepository.save(schedule);
    }

    private void validateNoLecturerConflict(ScheduleRequestDTO dto, Long excludeId) {
        if (scheduleRepository.existsLecturerConflict(
            dto.getLecturerId(), dto.getDay(), dto.getDurationId(), excludeId)) {
            throw new EntityValidationException(
                "Lecturer already has a class scheduled on this day at this time slot",
                Schedule.class, "conflict");
        }
    }

    @Transactional
    public void removeSchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new EntityNotFoundException(Schedule.class, String.valueOf(scheduleId));
        }
        scheduleRepository.deleteById(scheduleId);
    }

    public List<Schedule> findSchedulesRelatedToLecturer(Long lecturerId) {
        log.debug("Trying to get schedules related to lecturer. id: {}", lecturerId);
        return scheduleRepository.findByLecturerId(lecturerId);
    }

    @Transactional
    public void generateLessons(Long scheduleId, Long roomId, LocalDate from, LocalDate to) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new EntityNotFoundException(Schedule.class, String.valueOf(scheduleId)));

        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException(Room.class, String.valueOf(roomId)));

        java.time.DayOfWeek targetDay = java.time.DayOfWeek.valueOf(schedule.getDay().name());

        List<Lesson> lessons = from.datesUntil(to.plusDays(1))
            .filter(date -> date.getDayOfWeek() == targetDay)
            .map(date -> new Lesson(
                schedule.getDuration(),
                schedule.getSubject(),
                schedule.getGroup(),
                schedule.getLecturer(),
                room,
                date
            ))
            .collect(Collectors.toList());

        lessonRepository.saveAll(lessons);
        log.debug("Generated {} lessons from schedule id={}", lessons.size(), scheduleId);
    }
}
