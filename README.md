# FortechMicroservicesProject





![MicroServices Design]("https://www.imgur.com/a/AJ3d9")


**Microservices start order:<br><br>
    -> config-service<br>
    -> eureka-service<br>
    -> zuul-service<br>
    -> user-service<br>
    -> product-service<br>
    -> order-service<br>
    -> cart-service<br><br>
    -----------------------------------------------------------------------------------------------------------------------<br><br>
Every service is accessible via Zuul Server on `http://localhost:9999/{service-name}/{mapped_functionality}`.<br><br>
    -----------------------------------------------------------------------------------------------------------------------<br><br>
AMQP Messaging(RabbitMQ) depends on a RabbitMQ server which you will find here [HERE](https://www.rabbitmq.com).
<br><br>
I added to the main project folder a backup of the PostgreSQL database named _"online-shop"_, feel free to use it.**



Other : 

Use "git config --system core.longpaths true" if you get path too long error


To launch the angular client app : 

1) Open a command prompt in the project's root directory (APM - Start)

2) Type: npm install This installs the dependencies as defined in the package.json file.

3) Type: npm start This launches the TypeScript compiler (tsc) to compile the application and wait for changes. It also starts the lite-server and launches the browser to run the application.
