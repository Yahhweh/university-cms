package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.GroupService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    GroupService groupService;
    @MockitoBean
    FieldExtractor fieldExtractor;

    @Test
    void getGroups_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<String> fields = List.of("id", "name");
        List<Group> groups = List.of(new Group(), new Group());

        when(fieldExtractor.getFieldNames(Group.class)).thenReturn(fields);
        when(groupService.findAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("table"))
                .andExpect(model().attribute("fields", fields))
                .andExpect(model().attribute("objects", groups))
                .andExpect(model().attributeExists("fields", "objects"));

        verify(fieldExtractor).getFieldNames(Group.class);
        verify(groupService).findAllGroups();
    }
}