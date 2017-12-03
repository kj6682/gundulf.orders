package org.kj6682.gundulf.orders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles( profiles={"h2"})
public class SimpleOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SimpleOrderRepository simpleOrderRepository;

    // write test cases here
    @Test
    public void whenFindByProducerOrderByDeadline_thenReturnListOfOrders() {
        // given
        SimpleOrder simple = new SimpleOrder("Paris",
                "Four",
                "very delicious cookie",
                (short)1,
                (short)10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();

        // when
        List<SimpleOrder> found = simpleOrderRepository.findByProducerOrderByDeadline("Four");

        // then
        assertThat(!found.isEmpty());
        System.out.println("------------------------------");
        found.forEach(System.out::println);
    }

    @Test
    public void whenFindByShopOrderByCreated_thenReturnListOfOrders() {
        // given
        SimpleOrder simple = new SimpleOrder("Paris",
                "Entremets",
                "very delicious cake",
                (short)1,
                (short)10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();

        // when
        List<SimpleOrder> found = simpleOrderRepository.findByShopOrderByCreated("Paris");

        // then
        assertThat(!found.isEmpty());
        System.out.println("------------------------------");
        found.forEach(System.out::println);
    }

    @Test
    public void whenSaveAnExistingObject_thenUpdate() {
        // given
        SimpleOrder simple = new SimpleOrder("Paris",
                "Dummy",
                "very delicious cake",
                (short)1,
                (short)10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();


        List<SimpleOrder> found = simpleOrderRepository.findByProducerOrderByDeadline("Dummy");

        assertThat(!found.isEmpty());
        assertThat(found.size() == 1);
        assertThat(found.get(0).getQuantity() == 1);
        assertThat(found.get(0).getProduct().equals("very delicious cake"));

        //when
        SimpleOrder other = new SimpleOrder("Paris",
                "Dummy",
                "very delicious cake indeed",
                (short)1500,
                (short)10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        other.setId(found.get(0).getId());

        simpleOrderRepository.save(other);

        found = simpleOrderRepository.findByProducerOrderByDeadline("Dummy");

        // then
        assertThat(!found.isEmpty());
        assertThat(found.size() == 1);
        assertThat(found.get(0).getQuantity() == 1500);
        assertThat(found.get(0).getProduct().equals("very delicious cake indeed"));


    }
}