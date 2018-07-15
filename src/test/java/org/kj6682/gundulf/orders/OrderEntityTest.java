package org.kj6682.gundulf.orders;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"h2"})
public class OrderEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void customerNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order needs a customer");
        new Order(null,
                "address",
                "shop",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                new HashSet<Product>());
    }

    @Test
    public void addressNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order needs an address for delivery");
        new Order("customer",
                null,
                "shop",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                new HashSet<Product>());
    }

    @Test
    public void shopNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order must be attached to a shop");
        new Order("customer",
                "address",
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                new HashSet<Product>());
    }

    @Test
    public void createDateNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order must fixed creation date");
        new Order("customer",
                "address",
                "shop",
                null,
                LocalDate.now().plusDays(1),
                new HashSet<Product>());
    }

    @Test
    public void deadlineNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order must have a fixed deadline");
        new Order("customer",
                "address",
                "shop",
                LocalDate.now(),
                null,
                new HashSet<Product>());
    }

    @Test
    public void productsNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("an order must have a collection of products");
        new Order("customer",
                "address",
                "shop",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                null);
    }


}


