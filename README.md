# SG bank-account
 SG Bank Account

## Getting Started

To run this project, please follow those instructions

To build project with tests
```text
     mvn clean install
To build without unit tests :
```text
    mvn clean install -DskipTests
```

To start application
```text
    mvn spring-boot:run
```


## Documentation

You can access to the API Swagger at this URL:

```text
    http://localhost:8082/swagger-ui.html
```

## API

 to make a deposit or withdrawal operations on your bank account,
you need to get a user token with your credentials


With CURL :
```text
curl -X POST \
  localhost:8082/api/authentication/authenticate \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{"login":"aboumehdira", "password":"Lorena1674"} '
```  

Now you are able to call bank-account api's with an Authorization Bearer using token.

You can make POST Request at this URL in order to make a deposit or a withdrawal (testing account is 1001):  

```text
    http://localhost:8082/operations/
```  

With CURL :  

```CURL
    curl -X POST \
      http://localhost:8082/operations/ \
      -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjAzMzA2NzYzLCJleHAiOjE2MDM5MTE1NjN9.8-lATE3JxPKC6n6cC8NLpYmCKDlT5fAShk1aS4MB9_k4YTQsVuv0hqfNNaD6njZxpI9s5s9eViM3XHGwVMKiQw' \
      -H 'Content-Type: application/json' \
      -H 'cache-control: no-cache' \
      -d '{"accountNumber": 11223344556, "operationAmount": 1000}'
```  

to get operations of an account  :  

```text
    http://localhost:8082/operations/?accountNumber=11223344556
```  

With CURL :  

```CURL
    curl -X GET \
      'http://localhost:8082/operations/?accountNumber=11223344556' \
      -H 'Authorization: Bearer  eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjAzMzA2NzYzLCJleHAiOjE2MDM5MTE1NjN9.8-lATE3JxPKC6n6cC8NLpYmCKDlT5fAShk1aS4MB9_k4YTQsVuv0hqfNNaD6njZxpI9s5s9eViM3XHGwVMKiQw' \
      -H 'cache-control: no-cache'
```  


