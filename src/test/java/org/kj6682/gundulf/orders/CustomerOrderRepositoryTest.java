package org.kj6682.gundulf.orders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kj6682.gundulf.orders.orderline.OrderLine;
import org.kj6682.gundulf.orders.orderline.OrderLineRepository;
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
public class CustomerOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    // write test cases here
    @Test
    public void createSpecialOrder_OK() {
        // given
        OrderLine simple1 = new OrderLine("paris",
                "four",
                "very delicious cookie",
                10,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );

        OrderLine simple2 = new OrderLine("paris",
                "entremets",
                "very delicious cookie",
                20,
                LocalDate.of(2017,12,3),
                LocalDate.of(2017,12,4) );

        CustomerOrder so = new CustomerOrder();
        so.setDeadline(LocalDate.of(2018,01,31));
        so.getOrders().add(simple1);
        so.getOrders().add(simple2);

        Integer numberOfOrdersBefore = orderLineRepository.findAll().size();

        // when
        customerOrderRepository.save(so);

        // then
        List<CustomerOrder> sos = customerOrderRepository.findAll();
        assertThat(sos.size() != 0);

        Integer numberOfOrdersAfter = orderLineRepository.findAll().size();
        assertThat(numberOfOrdersAfter == (numberOfOrdersBefore + 2) );

    }

}