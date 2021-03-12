

Using postman

```POST at "http://localhost:8080/api/authenticate?username=user&password=password"```

You'll get the JWT key in the header.


```Then do a GET at http://localhost:8080/api/private```

But you must set a "Bearer Token" in the Authorization in Postman.

Put that JWT key that you got from the POST request as the "Bearer Token". But remove the "Bearer " text at the beginning of the JWT key.

