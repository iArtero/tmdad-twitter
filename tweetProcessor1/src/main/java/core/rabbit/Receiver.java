package core.rabbit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.tweetprocessors.EncryptService;
import core.tweetprocessors.VowelChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private final String exchangeName = "processed1";


    @Value("${processor.id}")
    private String processorId;
    @Autowired
    EncryptService encryptService;

    @Autowired
    VowelChangeService vowelChangeService;

    @Autowired
    RabbitEmisor rabbitEmisor;

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(byte[] bytes) {
        String message;
        try{
            message = new String(bytes, "UTF-8");

        }catch(UnsupportedEncodingException e){
            message = new String(bytes);
        }




        latch.countDown();

        ObjectMapper mapper = new ObjectMapper();


        GeneratedTweetDto generatedTweetDto = null;
        try {
            SearchedTweetDto searchedTweetDto = mapper.readValue(message, SearchedTweetDto.class);

            if (Integer.parseInt(processorId) == 1) {
                generatedTweetDto = encryptService.encryptTweet(searchedTweetDto);
            }
            if (Integer.parseInt(processorId) == 2) {
                generatedTweetDto = vowelChangeService.changeTweet(searchedTweetDto);
            }

            System.out.println("Received from <" + searchedTweetDto.getSearchedQuery()
                    + "> ; Operation<" + searchedTweetDto.getOperation() + ">");

            //messagingTemplate.convertAndSend("/queue/search/" + searchedTweetDto.getSearchedQuery(), generatedTweetDto);
            String tweetString = mapper.writeValueAsString(generatedTweetDto);
            try {
                rabbitEmisor.publish(tweetString, exchangeName);
            } catch (Exception e) {
                System.out.println("Error en publicación a broker");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //messagingTemplate.convertAndSend("/queue/search/" + name, tweet, map);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}