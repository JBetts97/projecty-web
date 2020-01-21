package com.projecty.projectyweb.notification;

import com.projecty.projectyweb.ProjectyWebApplication;
import com.projecty.projectyweb.project.Project;
import com.projecty.projectyweb.project.ProjectRepository;
import com.projecty.projectyweb.user.User;
import com.projecty.projectyweb.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectyWebApplication.class)
@AutoConfigureMockMvc
public class NotificationControllerTests {
    private static final String PROJECT_NAME = "My project";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    private Project project;
    private User user;

    @Before
    public void init() {
        user = new User();
        user.setId(1L);
        user.setUsername("user");

        project = new Project();
        project.setId(2L);
        project.setName(PROJECT_NAME);

        List<Notification> notifications = new ArrayList<>();

        Notification notification = new Notification();
        notification.setId(3L);
        notification.setType(Notifications.USER_ADDED_TO_PROJECT);
        notification.setUser(user);
        notification.setObjectIds(new Long[]{2L});
        notifications.add(notification);

        Mockito.when(notificationRepository.findAllByUser(user))
                .thenReturn(notifications);

        Mockito.when(notificationRepository.countByUserAndSeen(user, false))
                .thenReturn(567L);

        Mockito.when(projectRepository.findById(project.getId()))
                .thenReturn(java.util.Optional.of(project));

        Mockito.when(userRepository.findByUsername("user"))
                .thenReturn(java.util.Optional.of(user));

        // Unread
        Mockito.when(notificationRepository.findAllByUserAndSeen(user, false))
                .thenReturn(generateNotifications(123));
        // Read
        Mockito.when(notificationRepository.findAllByUserAndSeen(user, true))
                .thenReturn(generateNotifications(321));
    }

    private List<Notification> generateNotifications(int n) {
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Notification notification = new NotificationBuilder()
                    .setType(Notifications.USER_ADDED_TO_PROJECT)
                    .setObjectIds(new Long[]{project.getId()})
                    .setUser(user)
                    .createNotification();
            notifications.add(notification);
        }
        return notifications;
    }

    @Test
    @WithMockUser
    public void whenGetAllNotifications_shouldReturnAllNotifications() throws Exception {
        String excpectedNotification = messageSource.getMessage("user_added_to_project", new String[]{project.getName()}, Locale.getDefault());
        mockMvc.perform(get("/notifications"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[0].message").value(excpectedNotification))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenGetNotificationCount_shouldReturnNotificationCount() throws Exception {
        mockMvc.perform(get("/notifications/getUnreadNotificationCount"))
                .andExpect(jsonPath("$").value(567));
    }

    @Test
    @WithMockUser
    public void whenGetUnreadNotifications_shouldReturnUnreadNotifications() throws Exception {
        mockMvc.perform(get("/notifications?status=UNREAD"))
                .andExpect(jsonPath("$.length()").value(123));
    }

    @Test
    @WithMockUser
    public void whenGetReadNotifications_shouldReturnReadNotifications() throws Exception {
        mockMvc.perform(get("/notifications?status=READ"))
                .andExpect(jsonPath("$.length()").value(321));
    }
}
