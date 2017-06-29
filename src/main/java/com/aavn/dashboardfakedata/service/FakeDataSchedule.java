package com.aavn.dashboardfakedata.service;

import model.LocationPointMessage;
import model.NotificationMessage;
import model.ScheduleMatchMessage;
import org.apache.http.client.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeoutException;

import java.util.Random;

/**
 * Created by vqnguyen on 6/5/2017.
 */
@Service
public class FakeDataSchedule {
    private static String NOTI_EXCHANG_NAME = "generator.vibe.sent.fanout";
    private static String LOCATION_EXCHANGE_NAME= "travel-stats.location.fanout";
    private static String SCHEDULE_EXCHANGE_NAME= "travel-stats.schedule-match.fanout";
    private static String NOTIFICATION_MESSAGE_SMALL_DELAY = "small_delay";
    private static String NOTIFICATION_MESSAGE_ON_TIME = "on_time";
    private static String host = "192.168.78.50";
    private static String username = "test";
    private static String password = "test";
    private static double fakeLat = 59.914000;
    private static double fakeLon = 10.752175;
    static String[] connectionTrain = new String[]{"Olso Center Station", "NSB Nationaltheatret Train Station", "Okemvelem", "Grenseveien", "Kirkeveien"};

    @Scheduled(fixedRate  = 1000)
    public void sentNotification() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        Connection connection = null;
        Channel channel = null;
        Random rand = new Random();
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(NOTI_EXCHANG_NAME, "fanout", true);
            Random r = new Random();
            if (r.nextInt(10) > 5) {
                double lat = fakeLat + Math.pow(rand.nextDouble(),4);
                double lon = fakeLon + Math.pow(rand.nextDouble(),4);
                NotificationMessage notificationMessage = new NotificationMessage();
                notificationMessage.setLatitude(lat);
                notificationMessage.setLongitude(lon);
                notificationMessage.setDateTimeScheduled(OffsetDateTime.now().toString());
                notificationMessage.setVibeType(rand.nextInt(10) < 6 ? NOTIFICATION_MESSAGE_ON_TIME : NOTIFICATION_MESSAGE_SMALL_DELAY );
                ObjectMapper mapper = new ObjectMapper();
                String data = mapper.writeValueAsString(notificationMessage);
                channel.basicPublish(NOTI_EXCHANG_NAME, "", null, data.getBytes());
            }
            channel.close();
            connection.close();
            //System.out.println(" [x] Sent '" + notificationMessage + "'");
        } catch (TimeoutException e) {
            e.printStackTrace();
            connection.close();
        }

    }

    @Scheduled(fixedRate  = 5000)
    public void sentSchedule() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        Connection connection = null;
        Channel channel = null;
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(SCHEDULE_EXCHANGE_NAME, "fanout", true);
            Random r = new Random();

            if (r.nextInt(10) > 5) {
                ArrayList<ScheduleMatchMessage> listSchedule = new ArrayList<>();
                for (int j=0; j< 1 + r.nextInt(5); j++) {
                    ScheduleMatchMessage scheduleMatchMessage = new ScheduleMatchMessage();
                    scheduleMatchMessage.setArrivalStationName(connectionTrain[rand.nextInt(connectionTrain.length)]);
                    scheduleMatchMessage.setDepartureStationName(connectionTrain[rand.nextInt(connectionTrain.length)]);
                    cal.setTime(new Date());
                    cal.add(Calendar.MINUTE, -16);
                    scheduleMatchMessage.setStartTime(cal.getTimeInMillis());
                    scheduleMatchMessage.setEndTime(new Date().getTime());
                    scheduleMatchMessage.setConnectionName(connectionTrain[rand.nextInt(connectionTrain.length)]);
                    scheduleMatchMessage.setVid("fakedata-" + r.nextInt(100));
                    listSchedule.add(scheduleMatchMessage);
                }
                ObjectMapper mapper = new ObjectMapper();
                String data = mapper.writeValueAsString(listSchedule);
                channel.basicPublish(SCHEDULE_EXCHANGE_NAME, "", null, data.getBytes());
            }
            channel.close();
            connection.close();
            //System.out.println(" [x] Sent '" + notificationMessage + "'");
        } catch (TimeoutException e) {
            e.printStackTrace();
            connection.close();
        }

    }

    @Scheduled(fixedRate  = 5000)
    public void sentLocation() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        Connection connection = null;
        Channel channel = null;
        Random rand = new Random();
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(LOCATION_EXCHANGE_NAME, "fanout", true);

            Random r = new Random();
            List arrayPoint = new ArrayList<>();
            for (int i = 0; i < r.nextInt(50); i++) {
                double lat = fakeLat + rand.nextDouble() * Math.pow(rand.nextDouble(),4);
                double lon = fakeLon + rand.nextDouble() * Math.pow(rand.nextDouble(),4);
                LocationPointMessage locationMessage = new LocationPointMessage();
                locationMessage.setLatitude(lat);
                locationMessage.setLongitude(lon);
                locationMessage.setDatetimeCreated(new Date().getTime());
                locationMessage.setVid("fakedata-" + r.nextInt(100));
                arrayPoint.add(locationMessage);

            }
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(arrayPoint);
            channel.basicPublish(LOCATION_EXCHANGE_NAME, "", null, data.getBytes());
            channel.close();
            connection.close();
            //System.out.println(" [x] Sent '" + notificationMessage + "'");
        } catch (TimeoutException e) {
            e.printStackTrace();
            connection.close();
        }

    }

}
