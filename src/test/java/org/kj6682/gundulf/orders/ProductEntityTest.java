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


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"h2"})
public class ProductEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void nameNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a name");
        new Product(null, "category", 0, 1);
    }

    @Test
    public void categoryNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a category");
        new Product("product", null, 0, 1);
    }

    @Test
    public void quantityNotNull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a quantity");
        new Product("name", "category", null, 1);
    }

    @Test
    public void quantityNotNegative() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a positive quantity");
        new Product("name", "category", -1, 1);
    }

    @Test
    public void sizeNotnull() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a size");
        new Product("name", "category", 1, null);
    }

    @Test
    public void sizeNotNegative() throws Exception{
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("a product in an order needs a positive size");
        new Product("name", "category", 1, -1);
    }

}



