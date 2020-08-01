package com.softserve.edu;

import com.softserve.edu.model.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentRepositoryTest {

    private static final String EMAIL = "radio@gaga.fm";
    private static final String FIRST_NAME = "Freddy";
    private static final String LAST_NAME = "Mercury";
    private static final String PASSWORD = "password";
    private static final User.Role ROLE = User.Role.TRAINEE;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        userRepository.save(user);
    }

    @Test
    public void newUserTest() {
        User user = new User();
        user.setRole(User.Role.MENTOR);
        user.setEmail("slim@shady.fm");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("pass");
        userRepository.save(user);
        User actual = userRepository.findUserByEmail("slim@shady.fm");
        Assertions.assertEquals("firstName", actual.getFirstName());
    }

    @Test
    public void updateExistingUserTest() {
        User updatedUser = userRepository.findUserByEmail(EMAIL);
        updatedUser.setEmail("weWill@rock.ua");
        Long id = updatedUser.getId();
        userRepository.save(updatedUser);
        Assertions.assertNotEquals("radio@gaga.fm", userRepository.getOne(id).getEmail());
    }

    @Test
    public void deleteExistingUserTest() {
        Long id = userRepository.findUserByEmail(EMAIL).getId();
        userRepository.deleteById(id);
        Assertions.assertNull(userRepository.findUserByEmail(EMAIL));
    }

    @Test
    public void findAllUsersTest() {
        int expected = 1;
        int actual = userRepository.findAll().size();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    public void getAllByRoleTest() {
        User user = new User();
        user.setRole(User.Role.MENTOR);
        user.setEmail("slim@shady.fm");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("pass");
        userRepository.save(user);
        int expected = 2;
        int actual = userRepository.getAllByRole(User.Role.TRAINEE).size()
                + userRepository.getAllByRole(User.Role.MENTOR).size();
        Assertions.assertEquals(expected,actual);
    }
}
