# Manny
### Manny is a dubbo service router that can replace your dubbo consumer

Features

* Surport request: GET,POST,PUT,DELETE
* Base on spring webflux
* Parameter check
* Authorization support

# Examples

* Step1

Start a dubbo service

```java
public interface UserService {

    String hello();

    String sayHello(String name);

}
```

```java
@RequestRoute(value = "/user")
public class UserServiceImpl implements UserService {

    @Override
    @GetRoute("/hello")
    public String hello() {
        return "hello world";
    }

    @Override
    @PostRoute("/sayHello")
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
```

* Step2

Start manny server

```java
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
```

Then try http://localhost:port/user/hello

More detail see the manny-demo module

