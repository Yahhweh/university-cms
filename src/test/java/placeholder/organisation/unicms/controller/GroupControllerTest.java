package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.GroupService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void getGroups_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Group> groupList = List.of(new Group(), new Group());
        Page<Group> groupPage = new PageImpl<>(groupList);

        String sortField = "id";
        String sortDir = "asc";
        int pageNo = 1;

        when(groupService.getFilteredAndSortedGroup(anyString(), anyString(), anyInt()))
                .thenReturn(groupPage);

        mockMvc.perform(get("/groups")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDir)
                        .param("pageNo", String.valueOf(pageNo)))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attribute("groups", groupList))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDirection", sortDir))
                .andExpect(model().attribute("nextDir", "desc"))
                .andExpect(model().attributeExists("groups", "sortField", "sortDirection", "nextDir"));

        verify(groupService).getFilteredAndSortedGroup(sortField, sortDir, pageNo);
    }
}