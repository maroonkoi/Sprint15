package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarathonRepositoryTest {

    private static final String MARATHON_TITLE = "Marathon #1";

    @Autowired
    private MarathonRepository marathonRepository;

    @BeforeEach
    public void setUp() {
        Marathon marathon = new Marathon();
        marathon.setTitle(MARATHON_TITLE);
        marathonRepository.save(marathon);
    }

    @Test
    public void findMarathonByTitleTest() {
        Marathon expected = marathonRepository.findMarathonByTitle(MARATHON_TITLE);
        Assertions.assertEquals(expected.getTitle(), MARATHON_TITLE);
    }

    @Test
    public void findMarathonByIdTest(){
        Marathon existed = marathonRepository.findMarathonByTitle(MARATHON_TITLE);
        Optional<Marathon> expected = marathonRepository.findById(existed.getId());
        Assertions.assertTrue(expected.isPresent());
    }

    @Test
    public void saveNewMarathonTest(){
        Marathon marathon = new Marathon();
        marathon.setTitle("Marathon #2");
        marathonRepository.save(marathon);
        int expected = 2;
        Assertions.assertEquals(expected, marathonRepository.findAll().size());
    }

    @Test
    public void deleteMarathonByIdTest(){
        marathonRepository.deleteById(marathonRepository.findMarathonByTitle(MARATHON_TITLE).getId());
        Assertions.assertTrue(marathonRepository.findAll().isEmpty());
    }
}
