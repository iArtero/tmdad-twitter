package core.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class TweetController {


    @Value("${uri.tweetChooser}")
    private String tweetChooser;

    @MessageMapping("/search")
    public void search(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query) {

        String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetAccessFindByText = tweetChooser+"/search";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        BasicDBObject argument = (BasicDBObject) JSON.parse(query);
        String q = (String)argument.get("query");
        int op = (int)argument.get("mode");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetAccessFindByText)
                .queryParam("query", q)
                .queryParam("mode", op)
                .queryParam("sessionId", sessionId);


        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
    }


    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
