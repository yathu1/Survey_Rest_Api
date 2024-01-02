package com.yathu.springboot.restapi.helloworld;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {

    @RequestMapping("/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @RequestMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello Raj");
    }

    @RequestMapping("/hello-world/path-variable/{name}")
    public HelloWorldBean helloWorldPathParam(@PathVariable String name) {
        return new HelloWorldBean("Hello World, "+name);
    }
    @RequestMapping("/hello-world/path-variable/{name}/message/{message}")
    public HelloWorldBean helloWorldPathMultipleParam(@PathVariable String name,@PathVariable String message) {
        return new HelloWorldBean("Hello World, " + name +message);
    }
}
