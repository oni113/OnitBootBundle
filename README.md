application-private.yml   

spring:   
    datasource:   
        url: #{your database connect url}   
        username: #{your database user name}   
        password: #{your database user password}   
        hikari:   
            connection-test-query: #{your database}   
               
custom:   
    jwt:   
        secretKey: #{your secret key value}   