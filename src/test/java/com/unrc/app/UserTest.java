package com.unrc.app;

import com.unrc.app.models.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static org.javalite.test.jspec.JSpec.the;
import static org.junit.Assert.assertEquals;

public class UserTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/carsapp_test", "root", "");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void shouldValidateMandatoryFields(){
        User user = new User();

        the(user).shouldNotBe("valid");
        the(user.errors().get("first_name")).shouldBeEqual("value is missing");
        the(user.errors().get("last_name")).shouldBeEqual("value is missing");
        the(user.errors().get("email")).shouldBeEqual("value is missing");

        user.set("first_name", "John", "last_name", "Doe", "email", "example@email.com","is_admin",false);

        // Everything is good:
        the(user).shouldBe("valid");
    }
    @Test
    public void createUserTest(){
        User user = new User();
        user.set("first_name", "John", "last_name", "Doe", "email", "example@email.com","is_admin",true);
        user.createUser("John","Hanckok","hanckok@mail.com");
        User user2 = User.findFirst("email = ?","hanckok@mail.com");
        assertThat(user2.getBoolean("is_admin"),is(false));
    }
}
