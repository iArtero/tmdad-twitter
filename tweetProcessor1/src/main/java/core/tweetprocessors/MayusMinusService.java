package core.tweetprocessors;

import core.db.model.ConfigurationDto;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.IConfigurationRepository;
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

    @Autowired
    private IConfigurationRepository configurationRepository;

    @Value("${processor.config.key}")
    private String configKey;

    @Value("${processor.id}")
    private String processorId;


    private boolean mayus = true;


    public void changeMode(){

        ConfigurationDto conf =  configurationRepository.findOne(configKey+processorId);
        String value = String.valueOf(mayus);
        if(conf == null){
            conf = new ConfigurationDto();
            conf.setId(configKey+processorId);
        }else{
            value = conf.getValue();
            value = String.valueOf(mayus);
        }




        if(Boolean.parseBoolean(value)){
            mayus = false;
        }else {
            mayus = true;
        }
        value = String.valueOf(mayus);

        conf.setValue(value);

        configurationRepository.save(conf);
    }


    public MayusMinusService() {
        super();
    }

    public GeneratedTweetDto changeTweet(SearchedTweetDto tweet) {
        counterService.increment("counter.changedtweetscase.total");

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