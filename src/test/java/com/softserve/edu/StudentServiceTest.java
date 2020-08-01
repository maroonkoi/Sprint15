package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

    private static final String EMAIL = "radio@gaga.fm";
    private static final String FIRST_NAME = "Freddy";
    private static final String LAST_NAME = "Mercury";
    private static final String PASSWORD = "password";
    private static final User.Role STUDENT = User.Role.TRAINEE;
    private static final String MARATHON_TITLE_FIRST = "Marathon #1";

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarathonRepository marathonRepository;

    @BeforeEach
    public void setUp() {
        List<User> studentList = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            User user = new User();
            user.setRole(STUDENT);
            user.setId(Long.valueOf(i));
            user.setFirstName(FIRST_NAME + i);
            user.setLastName(LAST_NAME + i);
            user.setPassword(PASSWORD + i);
            user.setEmail(i + EMAIL);
            studentList.add(user);
        }
        Marathon marathon = new Marathon();
        marathon.setTitle(MARATHON_TITLE_FIRST);
        marathon.setId(1L);
        marathon.setUsers(new LinkedHashSet<>());
        marathon.getUsers().add(studentList.get(1));
        Mockito.when(userRepository.findAll()).thenReturn(studentList);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(studentList.get(0)));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(studentList.get(0));
        Mockito.when(userRepository.getAllByRole(STUDENT)).thenReturn(studentList);
        Mockito.when(userRepository.getOne(2L)).thenReturn(studentList.get(1));
        Mockito.when(marathonRepository.getOne(1L)).thenReturn(marathon);
        Mockito.when(marathonRepository.save(Mockito.any())).thenReturn(marathon);
    }

    @Test
    public void getAllStudenttest() {
        List<User> expacted = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            User user = new User();
            user.setRole(STUDENT);
            user.setId(Long.valueOf(i));
            user.setFirstName(FIRST_NAME + i);
            user.setLastName(LAST_NAME + i);
            user.setPassword(PASSWORD + i);
            user.setEmail(i + EMAIL);
            expacted.add(user);
        }
        List<User> actual = userService.getAll();
        Assertions.assertEquals(expacted, actual);
    }

    @Test
    public void getStudentByIdTest() {
        User expected = new User();
        expected.setEmail(1L + EMAIL);
        Assertions.assertEquals(expected.getEmail(), userService.getUserById(1L).getEmail());
    }

    @Test
    public void createOrUpdateUserTest() {
        User user = new User();
        user.setRole(STUDENT);
        user.setId(1L);
        user.setFirstName(FIRST_NAME + 1);
        user.setLastName(LAST_NAME + 1);
        user.setPassword(PASSWORD + 1);
        user.setEmail(1 + EMAIL);
        Assertions.assertEquals(user, userService.createOrUpdateUser(user));
    }

    @Test
    public void getAllStudentTest() {
        List<User> studentList = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            User user = new User();
            user.setRole(STUDENT);
            user.setId(Long.valueOf(i));
            user.setFirstName(FIRST_NAME + i);
            user.setLastName(LAST_NAME + i);
            user.setPassword(PASSWORD + i);
            user.setEmail(i + EMAIL);
            studentList.add(user);
        }
        List<User> actual = userService.getAllByRole(String.valueOf(STUDENT));
        Assertions.assertEquals(studentList, actual);
    }

    @Test
    public void addUserToMarathonTest() {
        User user = new User();
        user.setRole(STUDENT);
        user.setId(Long.valueOf(1));
        user.setFirstName(FIRST_NAME + 2);
        user.setLastName(LAST_NAME + 2);
        user.setPassword(PASSWORD + 2);
        user.setEmail(2 + EMAIL);
        Marathon marathon = new Marathon();
        marathon.setTitle(MARATHON_TITLE_FIRST);
        marathon.setId(1L);
        marathon.setUsers(new LinkedHashSet<>());
        Assertions.assertTrue(userService.addUserToMarathon(user, marathon));
    }

    @Test
    public void deleteUserFromMarathonTest() {
        User user = new User();
        user.setId(2L);
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        userService.deleteUserFromMarathon(user, marathon);
        Mockito.verify(marathonRepository).save(Mockito.any());
    }

    @Test
    public void deleteUserById() {
        userService.deleteUserById(1L);
        Mockito.verify(userRepository).deleteById(Mockito.any());
    }

}
