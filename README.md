# FortechMicroservicesProject

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