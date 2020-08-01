package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.service.MarathonService;
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

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class MarathonServiceTest {

    private static final String MARATHON_TITLE_FIRST = "Marathon #1";
    private static final String MARATHON_TITLE_SECOND = "Marathon #2";

    @Autowired
    private MarathonService marathonService;

    @MockBean
    private MarathonRepository marathonRepository;

    @BeforeEach
    public void setUp() {
        Marathon marathon1 = new Marathon();
        marathon1.setTitle(MARATHON_TITLE_FIRST);
        marathon1.setId(1L);
        Marathon marathon2 = new Marathon();
        marathon2.setTitle(MARATHON_TITLE_SECOND);
        marathon2.setId(2L);
        List<Marathon> marathonList = List.of(marathon1, marathon2);
        Mockito.when(marathonRepository.findAll()).thenReturn(marathonList);
        Mockito.when(marathonRepository.findById(1L)).thenReturn(Optional.of(marathon1));
        Mockito.when(marathonRepository.save(marathon2)).thenReturn(marathon2);
    }

    @Test
    public void getAllMarathonTest() {
        Marathon marathon1 = new Marathon();
        marathon1.setTitle(MARATHON_TITLE_FIRST);
        marathon1.setId(1L);
        Marathon marathon2 = new Marathon();
        marathon2.setTitle(MARATHON_TITLE_SECOND);
        marathon2.setId(2L);
        List<Marathon> expected = List.of(marathon1, marathon2);
        List<Marathon> actual = marathonService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getMarathonByIdTest() {
        Marathon expected = new Marathon();
        expected.setTitle(MARATHON_TITLE_FIRST);
        expected.setId(1L);
        Marathon actual = marathonService.getMarathonById(1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void marathonCreationTest() {
        Marathon expected = new Marathon();
        expected.setTitle(MARATHON_TITLE_SECOND);
        expected.setId(2L);
        Marathon actual = marathonService.createOrUpdate(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void deleteMarathonByIdTest() {
        marathonService.deleteMarathonById(1L);
        Mockito.verify(marathonRepository).deleteById(Mockito.any());
    }

}
