package core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;



public class AuthenticatedInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userId = null;
        try {
            String auth = request.getHeader("Authorization");


            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Authorization",auth);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://graph.facebook.com/me");//env.getProperty("uri.facebook.me"));

            HttpEntity<?> entity = new HttpEntity<>(headers);

            HttpEntity<String> facebookResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);



            Map<String,Object> body = new ObjectMapper().readValue(facebookResponse.getBody(), HashMap.class);
            userId =(String)body.get("id");




        }catch(Exception e){
            e.printStackTrace();
                response.getWriter().write(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        if (userId!=null){
            return true;
        }

        else{
            response.getWriter().write(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

}