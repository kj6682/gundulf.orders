package org.kj6682.gundulf.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles( profiles={"h2"})
public class ToDoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToDoRepository toDoRepository;

    ToDo toDo, toDoToday;

    @Before
    public void setup() throws Exception{
        toDo = new ToDo("product", 1, LocalDate.of(2018,07,16), 1);
        toDoToday = new ToDo("product", 1, LocalDate.now(), 1);
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
        assertThat(numberOfToDosAfter == (numberOfToDosBefore + 2) );

    }

    @Test
    public void findByDate(){
        //give toDoToday

        Integer numberOfToDosBefore = toDoRepository.findAll().size();

        // when
        toDoRepository.save(toDoToday);

        // then
        List<ToDo> sos = toDoRepository.findByDeadline(LocalDate.now());
        assertThat(sos.size() != 0);

        Integer numberOfToDosAfter = toDoRepository.findAll().size();
        assertThat(numberOfToDosAfter == (numberOfToDosBefore + 1) );
    }

}