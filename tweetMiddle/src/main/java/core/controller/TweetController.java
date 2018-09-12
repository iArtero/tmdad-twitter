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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
public class TweetController {


    @Value("${uri.tweetChooser}")
    private String tweetChooser;

    @MessageMapping("/search")
    public void search(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query) {
        searchToChooser(headerAccessor,query,null);
        /*String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetChooserSearchUri = tweetChooser+"/search";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        BasicDBObject argument = (BasicDBObject) JSON.parse(query);
        String q = (String)argument.get("query");
        int op = (int)argument.get("mode");

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
                String.class);*/
    }

    @MessageMapping("/searchauth")
    public void searchauth(SimpMessageHeaderAccessor headerAccessor, @RequestParam("query") String query, Principal principal) {
        searchToChooser(headerAccessor,query,principal);
    }


    private void searchToChooser(SimpMessageHeaderAccessor headerAccessor, String query, Principal principal){
        String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetChooserSearchUri = tweetChooser+"/search";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        BasicDBObject argument = (BasicDBObject) JSON.parse(query);
        String q = (String)argument.get("query");
        int op = (int)argument.get("mode");

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        map.add("query", q);
        map.add("mode",String.valueOf(op));
        map.add("sessionId",sessionId);

        if(principal != null){
            LinkedHashMap<String,String> linkedHashMap =
             (LinkedHashMap<String,String>)((UsernamePasswordAuthenticationToken)((OAuth2Authentication)principal)
                            .getUserAuthentication()).getDetails();
            map.add("facebook_name",linkedHashMap.get("name"));
            map.add("facebook_id",linkedHashMap.get("id"));

        }else{
            map.add("facebook_name","");
            map.add("facebook_id","");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetChooserSearchUri);


        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                //entity,
                request,
                String.class);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
