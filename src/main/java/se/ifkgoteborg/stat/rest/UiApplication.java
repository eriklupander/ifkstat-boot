package se.ifkgoteborg.stat.rest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2015-07-14
 * Time: 00:35
 * To change this template use File | Settings | File Templates.
 */
@SpringBootApplication
@RestController
public class UiApplication {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
