package core.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import core.db.model.SearchedTweetDto;
import core.db.model.SearchedWordDto;
import core.db.repository.ISearchedWordsRepository;
import core.tweetchoser.TwitterLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class TweetController {

    @Autowired
    TwitterLookupService twitter;

    @Autowired
    ISearchedWordsRepository searchedWordsRepository;

    @RequestMapping(value="/search", method=RequestMethod.POST)
    public String search(@RequestParam("query") String query,
                         @RequestParam("mode") String mode, @RequestParam("sessionId") String sessionId) {



        twitter.search(query,Integer.parseInt(mode),sessionId);

        SearchedWordDto searchedWord = new SearchedWordDto();
        searchedWord.setSessionId(sessionId);
        searchedWord.setWord(query);

        searchedWordsRepository.save(searchedWord);

        return "OK";
    }

    @RequestMapping(value="/stop", method=RequestMethod.POST)
    public String stop(@RequestParam("sessionId") String sessionId) {

        twitter.cancelSearch(sessionId);
        return "OK";
    }


    @MessageExceptionHandler(Exception.class)
    public void handleError(Exception exception) {
        System.err.println("Error in TweetController");
    }

}
