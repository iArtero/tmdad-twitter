package core.tweetprocessors;

import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.utils.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class MayusMinusService {

    /*@Autowired
    ISearchedTweetRepository searchedTweetRepository;
    @Autowired
    IGeneratedTweetRepository generatedTweetRepository;*/

    @Qualifier("counterService")
    @Autowired
    private CounterService counterService;

    @Value("${processor.id}")
    private String processorId;


    private boolean mayus = true;


    public void changeMode(){
        mayus = !mayus;
    }


    public MayusMinusService() {
        super();
    }

    public GeneratedTweetDto changeTweet(SearchedTweetDto tweet) {
        counterService.increment("counter.changedtweets.total");

        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textChanged = mayus?tweet.getText().toUpperCase():tweet.getText().toLowerCase();
        generatedTweetDto.setText(textChanged);
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());
        generatedTweetDto.setSearchedQuery(tweet.getSearchedQuery());
        generatedTweetDto.setOperation(tweet.getOperation());
        return generatedTweetDto;
    }

}