package com.example.broker.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.broker.actors.MervillTransportationActor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.broker.actors.LetterTransportationActor;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("parcelSystem");
    }

    @Bean
    public ActorRef letterTransportationActor(ActorSystem actorSystem) {
        ActorRef mervillTransportationActor = mervillTransportationActor(actorSystem);
        return actorSystem.actorOf(Props.create(LetterTransportationActor.class,mervillTransportationActor), "letterTransportationActor");
        //return actorSystem.actorOf(LetterTransportationActor(mervillTransportationActor), "letterTransportationActor");
    }

    @Bean
    public ActorRef mervillTransportationActor(ActorSystem actorSystem) {
        return actorSystem.actorOf(Props.create(MervillTransportationActor.class), "mervillTransportationActor");
        //return actorSystem.actorOf(Props.create(MervillTransportationActor.class), "mervillTransportationActor");
    }
}