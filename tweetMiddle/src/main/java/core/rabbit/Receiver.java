package core.rabbit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.db.model.GeneratedTweetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;



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


        try {
            GeneratedTweetDto generatedTweetDto = mapper.readValue(message, GeneratedTweetDto.class);
            System.out.println("Received from <" + generatedTweetDto.getSearchedQuery());
            messagingTemplate.convertAndSend("/queue/search/"+generatedTweetDto.getOperation() + "/" + generatedTweetDto.getSearchedQuery(), generatedTweetDto);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //messagingTemplate.convertAndSend("/queue/search/" + name, tweet, map);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}