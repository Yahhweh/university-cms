package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import placeholder.organisation.unicms.entity.Duration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FieldExtractorTest {

    @Test
    void getFieldNames_shouldReturnDurationFields_whenClassIsGiven() {
        FieldExtractor fieldExtractor= new FieldExtractor();
        List<String> expected = List.of("id", "start", "end");
        List<String> list =  fieldExtractor.getFieldNames(Duration.class);

        assertThat(list).isEqualTo(expected);
    }
}