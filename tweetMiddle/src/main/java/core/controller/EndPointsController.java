package core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class EndPointsController extends WebSecurityConfigurerAdapter {


    @Value("${uri.tweetAccess}")
    private String tweetAccessUri;

    @Value("${uri.tweetProcessor1}")
    private String tweetProcessor1;

    @Value("${uri.tweetProcessor2}")
    private String tweetProcessor2;

    @Value("${uri.tweetProcessor3}")
    private String tweetProcessor3;

    @Value("${uri.tweetSaver}")
    private String tweetSaver;

    @Value("${uri.tweetAccess}")
    private String tweetAccess;

    @Value("${uri.tweetChooser}")
    private String tweetChooser;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.antMatcher("/**").authorizeRequests()
                //.antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll()
                .antMatchers("/configProcessor").authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                //.and().csrf().ignoringAntMatchers("/logout").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().csrf().disable();
        // @formatter:on
    }

    @GetMapping("/findByTextContaining")
    public String findByTextContaining(String text, String page, String size) throws IOException {
        String tweetAccessFindByText = tweetAccessUri+"/searchedTweets/search/findByTextContaining";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetAccessFindByText)
                .queryParam("text", text)
                .queryParam("page", page)
                .queryParam("size", size);


        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);


        return response.getBody();

    }

    @PostMapping("/configProcessor")
    public HttpEntity configProcessor(@RequestParam("processor") String processor, Principal principal){

        int processorId = Integer.parseInt(processor);

        //CsrfToken token
        //        =session.getAttribute("HttpSessionCsrfTokenRepository.CSRF_TOKEN");
        String tweetProcessorUri = tweetProcessor1+"/change";

        if(processorId==2){
            tweetProcessorUri = tweetProcessor2+"/change";
        }else if(processorId==3){
            tweetProcessorUri = tweetProcessor3+"/change";
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();// httpHeaders;
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) ((OAuth2Authentication)principal).getDetails();

        headers.set("Authorization", details.getTokenType().replaceFirst("bearer","Bearer")+" "+details.getTokenValue());

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
       // map.add("principal", principalJSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tweetProcessorUri);
              //  .queryParam("principal",principal);
                //builder.queryParam("principal",principal);
        //HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);



        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                //entity,
                request,
                String.class);


        return response;

    }





    @GetMapping("/dashboardInfo")
    public Map<String,Object> dashboardInfo(@RequestParam("node") String node) {

        Map<String, Object> mapResult = new HashMap<>();

        String uriToConsult = "";

        switch (node){
            case "saver":  uriToConsult = tweetSaver;
                break;
            case "access":  uriToConsult = tweetAccess;
                break;
            case "chooser":  uriToConsult = tweetChooser;
                break;
            case "processor1":  uriToConsult = tweetProcessor1;
                break;
            case "processor2":  uriToConsult = tweetProcessor2;
                break;
            case "processor3":  uriToConsult = tweetProcessor3;
                break;
            default:
                break;

        }

        uriToConsult = uriToConsult+"/metrics";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uriToConsult);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response;

        Map<String,Object> body = null;

        try {

            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);


            body = new ObjectMapper().readValue(response.getBody(), HashMap.class);

            switch (node){
                case "chooser":
                    mapResult.put("counter.streams.total",body.get("counter.streams.total"));
                    mapResult.put("counter.streams.current",body.get("counter.streams.current"));
                    break;
                case"processor1":
                    mapResult.put("counter.encryptedtweets.total",body.get("counter.encryptedtweets.total"));
                    break;
                case "processor2":
                    mapResult.put("counter.encryptedtweets.total",body.get("counter.changedtweets.total"));
                    break;
                case "processor3":
                    mapResult.put("counter.encryptedtweets.total",body.get("counter.changedtweetsCase.total"));
                    break;

            }

            mapResult.put("health","UP");


        } catch (Exception e ) {
            e.printStackTrace();
        }

        mapResult.put("node",node);
        return mapResult;


    }



    @GetMapping("/user")
    public Principal getUser(Principal principal){
        return principal;
    }

    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
