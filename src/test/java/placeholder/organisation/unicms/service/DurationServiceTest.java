package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.repository.DurationRepository;
import placeholder.organisation.unicms.service.dto.DurationDTO;
import placeholder.organisation.unicms.service.mapper.DurationMapper;
import placeholder.organisation.unicms.service.validation.DurationValidation;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DurationServiceTest {
    @Mock
    DurationRepository durationRepository;
    @Spy
    DurationMapper durationMapper = Mappers.getMapper(DurationMapper.class);
    @InjectMocks
    DurationService durationService;

    @Test
    void givenValidDurationDto_whenUpdateDuration_thenDurationIsUpdated() {
        Duration initial = getDuration();
        DurationDTO changes = getDurationDto();
        long id = initial.getId();

        when(durationRepository.findById(id)).thenReturn(Optional.of(initial));

        durationService.updateDuration(id, changes);

        verify(durationMapper).updateEntityFromDto(changes, initial);
        verify(durationRepository).save(initial);

        assertThat(initial.getStart()).isEqualTo(changes.getStart());
        assertThat(initial.getEnd()).isEqualTo(changes.getEnd());
    }

    Duration getDuration(){
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00));
    }
    DurationDTO getDurationDto(){
        return new DurationDTO(LocalTime.of(10, 00), LocalTime.of(11, 30));
    }
}