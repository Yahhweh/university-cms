package placeholder.organisation.unicms.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Schedule;
import placeholder.organisation.unicms.repository.ScheduleRepository;
import placeholder.organisation.unicms.service.dto.request.ScheduleRequestDTO;
import placeholder.organisation.unicms.service.mapper.ScheduleMapper;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Log4j2
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void creatSchedule(ScheduleRequestDTO requestDTO){
        Schedule schedule = scheduleMapper.toEntity(requestDTO);
        log.debug("Schedule save successfully. Day: {}", schedule.getDay());
    }

    @Transactional
    public void updateSchedule(ScheduleRequestDTO requestDTO, Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException(Schedule.class, String.valueOf(scheduleId)));
        scheduleMapper.updateEntityFromDto(requestDTO, schedule);
    }

    @Transactional
    void removeSchedule(Long scheduleId){

    }
}
