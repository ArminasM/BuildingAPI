package demo;

import demo.controllers.RequestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RequestController controller;

    @Test
    public void contextLoads() throws Exception {
        Initialisation.main(new String[] {});
    }

}