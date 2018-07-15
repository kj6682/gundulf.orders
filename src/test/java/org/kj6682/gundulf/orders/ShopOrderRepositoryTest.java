package org.kj6682.gundulf.orders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles( profiles={"h2"})
public class ShopOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    Product cake1, cake4;

    ShopOrder shopOrder;

    @Before
    public void setup() throws Exception{
        cake1 = new Product("product",
                "category",1,1);
        cake4 = new Product("product",
                "category",1,4);

        Set<Product> products = new HashSet<Product>();
        products.add(cake1);
        products.add(cake4);

        shopOrder = new ShopOrder("customer",
                "address",
                "shop",
                LocalDate.of(2018, 07, 15),
                LocalDate.of(2018, 07, 16),
                products
        );

    }

    // write test cases here
    @Test
    public void createOrder_OK() {
        // given
        // shopOrder


        Integer numberOfOrdersBefore = productRepository.findAll().size();

        // when
        shopOrderRepository.save(shopOrder);

        // then
        List<ShopOrder> sos = shopOrderRepository.findAll();
        assertThat(sos.size() != 0);

        Integer numberOfOrdersAfter = productRepository.findAll().size();
        assertThat(numberOfOrdersAfter == (numberOfOrdersBefore + 2) );

    }

}