Using the following configuration:

```bash
spring:
  application:
    name: user-app

  cloud:
    discovery.client.simple.instances:
      say-hello:
        - instanceId: say-hello1
          uri: http://localhost:8080
        - instanceId: say-hello2
          uri: http://localhost:8081

    loadbalancer:
      configurations: request-based-sticky-session
      sticky-session:
        add-service-instance-cookie: true
        
server.port:9090        
```

the following call:

```bash
$ http :9090/hi 'Cookie:sc-lb-instance-id=say-hello1'
```

should go only to the `say-hello1` instance but it instead using round robin load balancing.

based on the (Request-based Sticky Session for LoadBalancer)[https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#request-based-sticky-session-for-loadbalancer]

With `RestTemplate` approach in the `RequestBasedStickySessionServiceInstanceListSupplier.get(Request request)` the 
`context` is not `RequestDataContext` which short circuits the instanceId selection and defaults back to Round Robin

With `WebClient` approach in the `RequestBasedStickySessionServiceInstanceListSupplier.get(Request request)` the 
`context` is a `RequestDataContext` but somehow there are no Cookies, however the RestController has access to the
`sc-lb-instance-id` cookie.
