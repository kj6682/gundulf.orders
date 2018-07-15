package org.kj6682.gundulf;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by luigi on 12/07/2017.
 * <p>
 * TDD - use this test to define the ORDER model
 */
@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.json.JsonTest
public class LocalDateTest {
    DateTimeFormatter dateformatter;

    DateTimeFormatter strangedateformatter;
    LocalDate onceUpon;

    @Before
    public void setup() throws Exception {
        dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        strangedateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        onceUpon = LocalDate.parse("1970-08-07", dateformatter);
    }


    @Test
    public void compare__String__And__LocalDate(){
        String formatted = "1970-08-07";
        assertThat(formatted.equals(onceUpon));

    }
    @Test
    public void compare__LocalDate__And__String(){

        String formatted = "1970-08-07";
        String aTime = onceUpon.format(dateformatter);
        assertThat(aTime.equals(formatted));

        String anotherTime = onceUpon.format(strangedateformatter);
        assertThat(!anotherTime.equals(aTime));


    }

    @Test
    public void compare__LocalDate__And__LocalDate(){

        String a = "1970-08-07";
        String b = "07/08/1970";

        LocalDate aTime = LocalDate.parse(a, dateformatter);
        LocalDate anotherTime = LocalDate.parse(b, strangedateformatter);
        assertThat(anotherTime.equals(aTime));


    }
}
