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
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SessionUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    /*@Autowired
    TwitterLookupService twitter;*/

    @Value("${uri.tweetChooser}")
    private String tweetChooserUri;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        //twitter.cancelSearch(headerAccessor);
        //TODO AQUI LLAMAR A CHOOOSER!!!!!!!

        String sessionId = headerAccessor.getSessionId(); // Gets session ID

        String tweetAccessFindByText = tweetChooserUri+"/stop";

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