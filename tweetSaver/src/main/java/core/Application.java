package core;


import core.tweetsaver.TweetSaver;
import org.springframework.amqp.core.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
@SpringBootApplication
public class Application {




    public static final String TWITTER_FANOUT_EXCHANGE = "tweets";

    static final String QUEUE_NAME_TWEET_SAVER = "QUEUE_TWEET_SAVER_1";

    @Bean
    Queue queueTweetSaver() {
        return new Queue(QUEUE_NAME_TWEET_SAVER, true);
    }

    @Bean
    TopicExchange twitterFanoutExchange2() {
        return new TopicExchange(TWITTER_FANOUT_EXCHANGE);
    }
    @Bean
    Binding binding2() {
        /*return BindingBuilder.bind(queueTweetSaver()).to(
                twitterFanoutExchange2());*/

        return BindingBuilder.bind(queueTweetSaver())
                .to(twitterFanoutExchange2())
                .with(TWITTER_FANOUT_EXCHANGE+".#");
    }



    @Bean
    SimpleMessageListenerContainer containerTweetSaver(ConnectionFactory connectionFactory,
                                                       MessageListenerAdapter listenerAdapterTweetSaver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_TWEET_SAVER);
        container.setMessageListener(listenerAdapterTweetSaver);

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterTweetSaver(TweetSaver tweetSaver) {
        return new MessageListenerAdapter(tweetSaver, "receiveMessage");
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

        };
    }

}