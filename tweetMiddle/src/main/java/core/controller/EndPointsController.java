package core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

@RestController
public class EndPointsController {


    @Value("${uri.tweetAccess}")
    private String tweetAccessUri;

    @Value("${uri.tweetProcessor1}")
    private String tweetProcessor1;

    @Value("${uri.tweetProcessor2}")
    private String tweetProcessor2;

    @Value("${uri.tweetSaver}")
    private String tweetSaver;

    @Value("${uri.tweetAccess}")
    private String tweetAccess;

    @Value("${uri.tweetChooser}")
    private String tweetChooser;

    //@Autowired
    //TwitterLookupService twitter;

    @GetMapping("/findByTextContaining")
    public String findByTextContaining(String text) throws IOException {
        String tweetAccessFindByText = tweetAccessUri+"/searchedTweets/search/findByTextContaining";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetAccessFindByText)
                .queryParam("text", text);


        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);


        return response.getBody();

    }

    @GetMapping("/configProcessor")
    public HttpEntity configProcessor(String processor) throws IOException {
        int processorId = Integer.parseInt(processor);


        String tweetProcessorUri = tweetProcessor1+"/change";

        if(processorId==2){
            tweetProcessorUri = tweetProcessor2+"/change";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetProcessorUri);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);


        return response;

    }

    @GetMapping("/dashboardInfo")
    public HttpEntity dashboardInfo() throws IOException {


        String tweetProcessor1Uri = tweetProcessor1+"/metrics";

        String tweetProcessor2Uri = tweetProcessor2+"/metrics";

        String tweetAccessUri = tweetAccess+"/metrics";

        String tweetSaverUri = tweetSaver+"/metrics";

        String tweetChooserUri = tweetChooser+"/metrics";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetProcessor1Uri);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        Map<String,Object> body = new ObjectMapper().readValue(response.getBody(), HashMap.class);


        Integer counterStreamsTotal = (Integer) body.get("counter.encryptedtweets.total");

        System.out.print(counterStreamsTotal);

        return response;

    }



    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
