package core.controller;


import core.tweetprocessors.EncryptService;
import core.tweetprocessors.VowelChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

import org.springframework.web.bind.annotation.*;


import java.io.IOException;



@RestController
public class ConfigController  {


    @Value("${uri.tweetAccess}")
    private String tweetAccessUri;

    @Value("${processor.id}")
    private String processorId;

    @Autowired
    EncryptService encryptService;

    @Autowired
    VowelChangeService vowelChangeService;



    @RequestMapping("/")
    public String index() throws IOException {
        return "Hello World!";
    }

    @RequestMapping("/change")
    public String change() throws IOException {
        int processor = Integer.parseInt(processorId);
        if(processor==1){
            encryptService.changeMode();
        }else if(processor==2){
            vowelChangeService.changeVowel();
        }

        return "{\"changed\": \"OK\"}";
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
