package org.kj6682.gundulf.todo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"h2"})
public class ToDoRepositoryTest {

    ToDo toDo, toDoToday, toDoWithDetails;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ToDoRepository toDoRepository;

    @Before
    public void setup() throws Exception {
        toDo = new ToDo("product", 1, LocalDate.of(2018, 07, 16), 1);
        toDoToday = new ToDo("product", 1, LocalDate.now(), 1);


        toDoWithDetails = new ToDo("product", 1, LocalDate.of(2018, 07, 16), 10);

        Detail d1 = new Detail("shop1", 8);
        Detail d2 = new Detail("shop2", 2);

        toDoWithDetails.addDetail(d1);
        toDoWithDetails.addDetail(d2);


    }

    // write test cases here
    @Test
    public void createOrder_OK() {
        // given toDo

        Integer numberOfToDosBefore = toDoRepository.findAll().size();

        // when
        toDoRepository.save(toDo);

        // then
        List<ToDo> sos = toDoRepository.findAll();
        assertThat(sos.size() != 0);

        Integer numberOfToDosAfter = toDoRepository.findAll().size();
        assertThat(numberOfToDosAfter == (numberOfToDosBefore + 2));

    }

    @Test
    public void findByDate() {
        //give toDoToday

        Integer numberOfToDosBefore = toDoRepository.findAll().size();

        // when
        toDoRepository.save(toDoToday);

        // then
        List<ToDo> sos = toDoRepository.findByDeadline(LocalDate.now());
        assertThat(sos.size() != 0);

        Integer numberOfToDosAfter = toDoRepository.findAll().size();
        assertThat(numberOfToDosAfter == (numberOfToDosBefore + 1));
    }

    @Test
    public void findByProduct() {
        //give toDoToday

        // when
        toDoRepository.save(toDoToday);

        // then
        List<ToDo> sos = toDoRepository.findByProduct(toDoToday.getProduct());
        assertThat(sos.size() != 0);
        assertThat(sos.size() == 1);


    }

    @Test
    public void createOrderWithDetails() {
        // given toDoWithDetails
        // when
        ToDo saved = toDoRepository.save(toDoWithDetails);
        // then
        assertThat(!saved.getDetails().isEmpty());
    }

    @Test
    public void updateOrderWithDetails() {
        // given toDoWithDetails
        ToDo saved = toDoRepository.save(toDoWithDetails);
        assertThat(!saved.getDetails().isEmpty());

        // when
        saved.update("shop1", 10);
        toDoRepository.save(toDoWithDetails);

        // then
        Set<Detail> details = saved.getDetails();
        Detail dt = details.stream().filter(p->p.getShop().equals("shop1")).findFirst().get();
        System.out.println(dt.getQuantity());
        int q = dt.getQuantity().intValue();

        Assert.assertEquals(q, 18);

        System.out.println("humpaloompa !!!!!!!");
        saved.getDetails().forEach(d -> System.out.println(d));
        System.out.println(dt);

    }

}