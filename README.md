# Fortech Microservices Project





![MicroServices Design](http://i.imgur.com/xmjojbY.png)


Netflix Open Source Software:

Eureka Service:

![Eureka Service Descovery](http://i.imgur.com/tzN0TVw.jpg)

Config Service
<br>
Zull Service
<br>
Hystrix Service
<br>



**Microservices start order:<br><br>
    -> config-service<br>
    -> eureka-service<br>
    -> zuul-service<br>
    -> user-service<br>
    -> product-service<br>
    -> order-service<br>
    -> cart-service<br>
    -> angularui-service<br><br>
    -----------------------------------------------------------------------------------------------------------------------<br><br>
Every service is accessible via Zuul Server on `http://localhost:9999/{service-name}/{mapped_functionality}`.<br><br>
    -----------------------------------------------------------------------------------------------------------------------<br><br>
AMQP Messaging(RabbitMQ) depends on a RabbitMQ server which you will find here [HERE](https://www.rabbitmq.com).
<br><br>
I added to the main project folder a backup of the PostgreSQL database named _"online-shop"_, feel free to use it.**


-----------------------------------------------------------------------------------------------------------------------<br><br>
Other : 

Use "git config --system core.longpaths true" if you get path too long error


To launch the angular client app : 

1) Open a command prompt in the project's root directory (APM - Start)

2) Type: npm install This installs the dependencies as defined in the package.json file.

3) Type: npm start This launches the TypeScript compiler (tsc) to compile the application and wait for changes. It also starts the lite-server and launches the browser to run the application.

-----------------------------------------------------------------------------------------------------------------------<br><br>
