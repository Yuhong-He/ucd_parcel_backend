package com.example.postman.message;

import com.alibaba.fastjson2.JSON;
import com.example.postman.dto.Parcel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQ {
    private static String address = "amqps://ucd_parcel_admin:BylaSoDu7byPasF2@b-2cb6f3fb-3957-4aa5-ad43-ca2b22178e62.mq.eu-west-1.amazonaws.com:5671/ucd_parcel";
    private static Boolean durable = false;
    private static Boolean autoAck = true;

    @Autowired
    private void setStaticFields(
            @Value("${MQ.address}") String s,
            @Value("#{new Boolean('${MQ.durable}')}") Boolean b1,
            @Value("#{new Boolean('${MQ.autoAck}')}") Boolean b2){
        address = s;
        durable = b1;
        autoAck = b2;
    }

    public static void sendToDatabase(Parcel parcel) throws Exception {
        String message = JSON.toJSONString(parcel);
        establishConnection().basicPublish("", "Parcel", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        log.info("Sending Log: " + message + " to Log System...");
    }

    public static Channel establishConnection() throws Exception {
        log.info("Connecting to rabbitMQServer:"+address+" ...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(address);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("Parcel", durable, false, false, null);
        return channel;
    }



}
