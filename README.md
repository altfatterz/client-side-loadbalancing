```bash
$ http :9090/hi 'Cookie:sc-lb-instance-id=say-hello1'
```

This request should go only to the `say-hello` instance but it instead using round robin load balancing.
