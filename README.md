# Manny

### Manny is a dubbo service gateway that can replace your dubbo consumer

Features

* Http method: GET,POST,PUT,DELETE
* Base on spring webflux
* Dubbo tag route
* Authorization

# Examples

* Step1

Start a dubbo provider service

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

Start a gateway bootstrap

```java

@SpringBootApplication
public class BootstrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
```

Then just trying http://localhost:{port}/user/hello

More detail see the manny-demo module

