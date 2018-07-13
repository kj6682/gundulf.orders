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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = {"h2"})
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void insertOneProduct() {
        // given
        Product simple = new Product("product", "category", 1, 1);
        repo.save(simple);
        Product other = repo.findOne(1L);

        // then
        assertThat(other != null);
     }

    @Test
    public void updateOneProduct() {
        // given
        Product one = new Product("product", "category",1,1);
        repo.save(one);

        Product two = repo.findOne(one.getId());
        assertThat(two != null);
        assertThat(two.equals(one));

        // then
        two.setSize(2);
        repo.save(two);

        Product three = repo.findOne(one.getId());
        assertThat(three != null);
        assertThat(!one.equals(three) );
        assertThat( three.getSize() == 2);

    }

    @Test
    public void deleteOneProduct() {
        // given
        Product one = new Product("product", "category", 1,1 );
        repo.save(one);

        Product two = repo.findOne(1L);
        assertThat(two != null);
        assertThat(two.equals(one));

        //when
        repo.delete(1L);

        //then
        Product three = repo.findOne(1L);
        assertThat(three == null);

    }


}//:)



