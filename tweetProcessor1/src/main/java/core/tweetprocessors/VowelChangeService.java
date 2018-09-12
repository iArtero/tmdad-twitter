package core.tweetprocessors;

import core.db.model.ConfigurationDto;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.IConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class VowelChangeService {

    @Qualifier("counterService")
    @Autowired
    private CounterService counterService;

    @Autowired
    private IConfigurationRepository configurationRepository;

    @Value("${processor.config.key}")
    private String configKey;

    @Value("${processor.id}")
    private String processorId;

    private  char vowelToChange = 'a';

    private final char[] vowels = {'a','e','i','o','u'};

    private int i = 0;

    public VowelChangeService() {
        super();
    }

    public void changeVowel(){


        ConfigurationDto conf =  configurationRepository.findOne(configKey+processorId);
        int value = i;

        if(conf == null){
            conf = new ConfigurationDto();
            conf.setId(configKey+processorId);
        }else{
            i = Integer.parseInt(conf.getValue());
        }

        i++;
        i = i%vowels.length;
        vowelToChange = vowels[i];

        conf.setValue(i+"");

        configurationRepository.save(conf);

    }

    public GeneratedTweetDto changeTweet(SearchedTweetDto tweet) {
        counterService.increment("counter.changedtweets.total");

        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textChanged = tweet.getText()
                .replace('a', vowelToChange)
                .replace('e', vowelToChange)
                .replace('i', vowelToChange)
                .replace('o', vowelToChange)
                .replace('u', vowelToChange)
                .replace("A", (vowelToChange+"").toUpperCase())
                .replace("E", (vowelToChange+"").toUpperCase())
                .replace("I", (vowelToChange+"").toUpperCase())
                .replace("O", (vowelToChange+"").toUpperCase())
                .replace("U", (vowelToChange+"").toUpperCase());
        generatedTweetDto.setText(textChanged);
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());
        generatedTweetDto.setSearchedQuery(tweet.getSearchedQuery());
        generatedTweetDto.setOperation(tweet.getOperation());
        return generatedTweetDto;
    }

}