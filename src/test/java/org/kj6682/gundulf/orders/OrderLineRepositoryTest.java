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

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles( profiles={"h2"})
public class OrderLineRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderLineRepository orderLineRepository;

    // write test cases here
    @Test
    public void whenFindByProducerOrderByDeadline_thenReturnListOfOrders() {
        // given
        OrderLine simple = new OrderLine("paris",
                "four",
                "very delicious cookie",
                10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();

        // when
        List<OrderLine> found = orderLineRepository.findByProducerOrderByDeadlineAndProductAsc("Four");

        // then
        assertThat(!found.isEmpty());
        System.out.println("------------------------------");
        found.forEach(System.out::println);
    }

    @Test
    public void whenFindByShopOrderByCreated_thenReturnListOfOrders() {
        // given
        OrderLine simple = new OrderLine("paris",
                "entremets",
                "very delicious cake",
                10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();

        // when
        List<OrderLine> found = orderLineRepository.findByShopOrderByDeadlineAndProductAsc("Paris");

        // then
        assertThat(!found.isEmpty());
        System.out.println("------------------------------");
        found.forEach(System.out::println);
    }

    @Test
    public void whenSaveAnExistingObject_thenUpdate() {
        // given
        OrderLine simple = new OrderLine("paris",
                "dummy",
                "very delicious cake",
                10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        entityManager.persist(simple);
        entityManager.flush();


        List<OrderLine> found = orderLineRepository.findByProducerOrderByDeadlineAndProductAsc("dummy");

        assertThat(!found.isEmpty());
        assertThat(found.size() == 1);
        assertThat(found.get(0).getQuantity() == 1);
        assertThat(found.get(0).getProduct().equals("very delicious cake"));

        //when
        OrderLine other = new OrderLine("paris",
                "dummy",
                "very delicious cake indeed",
                1500,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );
        other.setId(found.get(0).getId());

        orderLineRepository.save(other);

        found = orderLineRepository.findByProducerOrderByDeadlineAndProductAsc("dummy");

        // then
        assertThat(!found.isEmpty());
        assertThat(found.size() == 1);
        assertThat(found.get(0).getQuantity() == 1500);
        assertThat(found.get(0).getProduct().equals("very delicious cake indeed"));


    }
}