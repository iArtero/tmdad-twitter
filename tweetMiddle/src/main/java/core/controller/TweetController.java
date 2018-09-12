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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Optional;

@Controller
public class TweetController {


    @Value("${uri.tweetChooser}")
    private String tweetChooser;

    @MessageMapping("/search")
    public void search(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query) {

        String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetChooserSearchUri = tweetChooser+"/search";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        BasicDBObject argument = (BasicDBObject) JSON.parse(query);
        String q = (String)argument.get("query");
        int op = (int)argument.get("mode");

        /*UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetAccessFindByText)
                .queryParam("query", q)
                .queryParam("mode", op)
                .queryParam("sessionId", sessionId);
*/

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        map.add("query", q);
        map.add("mode",String.valueOf(op));
        map.add("sessionId",sessionId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetChooserSearchUri);


        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                //entity,
                request,
                String.class);

        /*HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);*/
    }


    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
