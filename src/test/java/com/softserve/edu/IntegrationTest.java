package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.MarathonService;
import com.softserve.edu.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Transactional
@ExtendWith(SprpluginingExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    private static final String EMAIL = "radio@gaga.fm";
    private static final String FIRST_NAME = "Freddy";
    private static final String LAST_NAME = "Mercury";
    private static final String PASSWORD = "password";
    private static final User.Role STUDENT = User.Role.TRAINEE;
    private static final User.Role MENTOR = User.Role.MENTOR;
    private static final String MARATHON_TITLE = "Marathon #";

    private final UserService userService;
    private final MarathonService marathonService;
    private final MarathonRepository marathonRepository;
    private final UserRepository userRepository;

    @Autowired
    public IntegrationTest(MarathonService marathonService,
                           UserService userService,
                           MarathonRepository marathonRepository,
                           UserRepository userRepository) {
        this.marathonService = marathonService;
        this.userService = userService;
        this.marathonRepository = marathonRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    private void fillDataService() {
        for (long i = 1; i <= 4; i++) {
            User user = new User();
            if (i > 3) {
                user.setRole(MENTOR);
            } else {
                user.setRole(STUDENT);
            }
            user.setFirstName(FIRST_NAME + i);
            user.setLastName(LAST_NAME + i);
            user.setPassword(PASSWORD + i);
            user.setEmail(i + EMAIL);
            userService.createOrUpdateUser(user);
        }
        for (long i = 1; i <= 3; i++) {
            Marathon marathon = new Marathon();
            marathon.setTitle(MARATHON_TITLE + i);
            marathon.setUsers(new LinkedHashSet<>());
            marathonService.createOrUpdate(marathon);
        }
    }

    @AfterEach
    public void clearData() {
        userRepository.deleteAll();
        marathonRepository.deleteAll();
    }

    @Test
    public void checkAllMarathonTest() {
        List<Marathon> expected = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            Marathon marathon = new Marathon();
            marathon.setTitle(MARATHON_TITLE + i);
            expected.add(marathon);
        }
        List<Marathon> actual = marathonService.getAll();
        Assertions.assertEquals(expected, actual, "checkAllMarathonTest");
    }

    @Test
    public void checkGetMarathonById() {
        String expected = MARATHON_TITLE + 3;
        Marathon marathon = marathonService.getMarathonByTitle(MARATHON_TITLE + 3);
        Long id = marathon.getId();
        Marathon actual = marathonService.getMarathonById(id);
        Assertions.assertEquals(expected, actual.getTitle(), "checkGetMarathonById");
    }

    @Test
    public void checkDeleteMarathonByIdTest() {
        Long id = marathonService.getMarathonByTitle(MARATHON_TITLE + 2).getId();
        marathonService.deleteMarathonById(id);
        Integer expected = 2;
        Integer actual = marathonService.getAll().size();
        Assertions.assertEquals(expected, actual, "checkGetMarathonById");
    }

    @Test
    public void checkCreationOfMarathonTest() {
        Marathon marathon = new Marathon();
        marathon.setTitle(MARATHON_TITLE);
        Assertions.assertNotNull(marathonService.createOrUpdate(marathon));
    }

    @Test
    public void checkGetAllStudentsTest() {
        List<User> expected = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            User user = new User();
            user.setRole(STUDENT);
            user.setId(i);
            user.setFirstName(FIRST_NAME + i);
            user.setLastName(LAST_NAME + i);
            user.setPassword(PASSWORD + i);
            user.setEmail(i + EMAIL);
            expected.add(user);
        }
        List<User> actual = userService.getAllByRole(User.Role.TRAINEE.toString());
        Assertions.assertEquals(expected, actual, "checkGetAllStudents()");
    }

    @Test
    public void checkGetUserByIdTest() {
        User userFromBD = userService.getAll().get(0);
        User expected = new User();
        expected.setRole(userFromBD.getRole());
        expected.setId(userFromBD.getId());
        expected.setFirstName(userFromBD.getFirstName());
        expected.setLastName(userFromBD.getLastName());
        expected.setPassword(userFromBD.getPassword());
        expected.setEmail(userFromBD.getEmail());
        User actual = userService.getUserById(userFromBD.getId());
        Assertions.assertEquals(expected, actual, "checkGetUserByIdTest()");
    }

    @Test
    public void checkCreateNewUserTest() {
        User newUser = new User();
        newUser.setRole(STUDENT);
        newUser.setFirstName("newUserFirstName");
        newUser.setLastName("newUserLastName");
        newUser.setPassword("parol");
        newUser.setEmail("newuser@mail.ua");
        int expected = 5;
        userService.createOrUpdateUser(newUser);
        int actual = userService.getAll().size();
        Assertions.assertEquals(expected, actual, "checkCreateNewUserTest()");
    }

    @Test
    public void checkUpdateNewUserTest() {
        User expected = userRepository.findUserByEmail(1 + EMAIL);
        Long expectedId = expected.getId();
        expected.setRole(STUDENT);
        expected.setFirstName("newUserFirstName");
        expected.setLastName("newUserLastName");
        expected.setPassword("parol");
        expected.setEmail("newuser@mail.ua");
        userService.createOrUpdateUser(expected);
        User actual = userService.getUserById(expectedId);
        Assertions.assertEquals(expected, actual, "checkUpdateNewUserTest()");
    }

    @Test
    public void checkGetAllByRoleTest() {
        int actual = userService.getAllByRole(User.Role.TRAINEE.toString()).size();
        int expected = 3;
        Assertions.assertEquals(expected, actual, "checkGetAllByRoleTest()");
    }

    @Test
    public void checkDeleteUserByIdTest() {
        int actual = userService.getAll().size() - 1;
        User deleted = userService.getAll().get(0);
        userRepository.delete(deleted);
        int expected = 3;
        Assertions.assertEquals(expected, actual, "checkDeleteUserByIdTest()");
    }

    @Test
    public void checkAddUserToMarathonTest() {
        List<User> users = userService.getAll();
        Marathon marathon = marathonService.getMarathonByTitle(MARATHON_TITLE + 1);
        Integer expected = users.size();
        for (User user : users) {
            userService.addUserToMarathon(user, marathon);
        }
        marathonService.createOrUpdate(marathon);
        Integer actual = marathonService.getMarathonByTitle(MARATHON_TITLE + 1).getUsers().size();
        Assertions.assertEquals(expected, actual, "checkAddUserToMarathonTest()");
    }

    @Test
    public void checkDeleteUserFromMarathonTest() {
        List<User> users = userService.getAll();
        Marathon marathon = marathonService.getMarathonByTitle(MARATHON_TITLE + 1);
        for (User user : users) {
            userService.addUserToMarathon(user, marathon);
        }
        marathonService.createOrUpdate(marathon);
        Set<User> marathonUsers = marathonService.getMarathonByTitle(MARATHON_TITLE + 1).getUsers();
        Integer actual = marathonUsers.size() - 1;

        User deleted = new ArrayList<>(marathonUsers).get(0);
        userService.deleteUserFromMarathon(deleted, marathon);
        Integer expected = marathonService.getMarathonByTitle(MARATHON_TITLE + 1).getUsers().size();
        Assertions.assertEquals(expected, actual, "checkAddUserToMarathonTest()");
    }
}
