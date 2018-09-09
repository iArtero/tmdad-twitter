package core.controller;

import core.rabbit.Receiver;
import core.tweetprocessors.EncryptService;
import core.tweetprocessors.VowelChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class ConfigController {


    @Value("${uri.tweetAccess}")
    private String tweetAccessUri;

    @Value("${processor.id}")
    private String processorId;

    @Autowired
    EncryptService encryptService;

    @Autowired
    VowelChangeService vowelChangeService;

    @GetMapping("/change")
    public String change() throws IOException {
        int processor = Integer.parseInt(processorId);
        if(processor==1){
            encryptService.changeMode();
        }else if(processor==2){
            vowelChangeService.changeVowel();
        }

        return "{\"status\": \"OK\"}";

    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
