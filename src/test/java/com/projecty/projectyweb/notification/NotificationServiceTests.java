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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectyWebApplication.class)
public class NotificationServiceTests {
    private static final String PROJECT_NAME = "PROJECT_SAMPLE_NAME";

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private ProjectRepository projectRepository;

    private Project project;
    private User user;

    @Before
    public void init() {
        user = new User();
        user.setUsername("notification-user");
        userRepository.save(user);
        project = new Project();
        project.setId(35L);
        project.setName(PROJECT_NAME);
        Mockito.when(projectRepository.findById(project.getId()))
                .thenReturn(java.util.Optional.of(project));
    }

    @Test
    public void whenCreateStringArgumentsArray_shouldReturnStringArgumentsArray() {
        String[] values = notificationService.createStringArgumentValuesArray(Notifications.USER_ADDED_TO_PROJECT, new Long[]{35L});
        assertThat(values[0], equalTo(project.getName()));
    }

    @Test
    public void whenGetMessageFromMessageSource_shouldReturnFormattedString() {
        Notifications type = Notifications.USER_ADDED_TO_PROJECT;
        String[] args = new String[1];
        String projectName = "Projective project";
        args[0] = projectName;
        String expected = messageSource.getMessage("user_added_to_project", args, Locale.getDefault());
        assertThat(notificationService.getMessageFromMessageSource(type, args), is(expected));
    }

    @Test
    public void whenGetNotificationString_shouldReturnGeneratedNotificationString() {
        Notification notification = new Notification();
        notification.setType(Notifications.USER_ADDED_TO_PROJECT);
        notification.setObjectIds(new Long[]{project.getId()});
        String excpected = messageSource.getMessage("user_added_to_project", new String[]{project.getName()}, Locale.getDefault());
        assertThat(notificationService.getNotificationString(notification), equalTo(excpected));
    }

    @Test
    public void whenSaveNotification_shouldReturnNotification() {
        Notification notification = notificationService.createNotificationAndSave(
                user, Notifications.USER_ADDED_TO_PROJECT, new Long[]{project.getId()});
        assertThat(notificationService.findById(notification.getId()).get(), equalTo(notification));
    }

    @Test
    public void whenGetUnreadNotificationCount_shouldReturnUnreadNotificationCount() {
        notificationService.createNotificationAndSave(user, Notifications.USER_ADDED_TO_PROJECT, new Long[]{project.getId()});
        long unreadNotificationCount = notificationService.getUnreadNotificationCountForSpecifiedUser(user);
        assertThat(unreadNotificationCount, greaterThan(0L));
    }

}
