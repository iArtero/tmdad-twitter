package core.eventlisteners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    /*@Autowired
    TwitterLookupService twitter;*/

    @Value("${uri.tweetChooser}")
    private String tweetChooserUri;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        //twitter.cancelSearch(headerAccessor);
        //TODO AQUI LLAMAR A CHOOOSER!!!!!!!

        String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetAccessFindByText = "http://"+tweetChooserUri+"/stop";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetAccessFindByText)

                .queryParam("sessionId", sessionId);


        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

    }

}