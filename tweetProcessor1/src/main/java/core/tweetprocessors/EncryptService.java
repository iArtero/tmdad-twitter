package core.tweetprocessors;

import core.db.model.ConfigurationDto;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.repository.IConfigurationRepository;
import core.utils.Encryptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptService {

    /*@Autowired
    ISearchedTweetRepository searchedTweetRepository;
    @Autowired
    IGeneratedTweetRepository generatedTweetRepository;*/


    @Autowired
    IConfigurationRepository configurationRepository;

    @Qualifier("counterService")
    @Autowired
    private CounterService counterService;

    @Value("${processor.config.key}")
    private String configKey;

    @Value("${processor.id}")
    private String processorId;

    @Value("${encrypt.aes.key}")
    private String aesKey;

    private boolean compressed = false;


    public void changeMode(){

        ConfigurationDto conf =  configurationRepository.findOne(configKey+processorId);
        String value = String.valueOf(compressed);

        if(conf == null){
            conf = new ConfigurationDto();

            conf.setId(configKey+processorId);
        }else{
            value = conf.getValue();
            value = String.valueOf(compressed);
        }


        if(Boolean.parseBoolean(value)){
            compressed = false;
        }else {
            compressed = true;
        }
        value = String.valueOf(compressed);

        conf.setValue(value);

        configurationRepository.save(conf);
    }


    public EncryptService() {
        super();
    }

    private GeneratedTweetDto copyAndEncryptTweet(Encryptor encryptor, SearchedTweetDto tweet) {
        counterService.increment("counter.encryptedtweets.total");

        GeneratedTweetDto generatedTweetDto = new GeneratedTweetDto();

        generatedTweetDto.setIdReferenced(tweet.getIdStr());
        String textEncrypted = encryptor.aesEncryptor(tweet.getText(), aesKey);
        generatedTweetDto.setText(textEncrypted);

        if(compressed && textEncrypted.length() > 40){
            generatedTweetDto.setText(textEncrypted.substring(0,10)+" ... "
                    +textEncrypted.substring(textEncrypted.length()-11,textEncrypted.length()-1));
        }
        generatedTweetDto.setPlainText(tweet.getText());
        generatedTweetDto.setFromUser(tweet.getFromUser());

        generatedTweetDto.setSearchedQuery(tweet.getSearchedQuery());
        generatedTweetDto.setOperation(tweet.getOperation());

        return generatedTweetDto;
    }

    public GeneratedTweetDto encryptTweet(SearchedTweetDto tweet) {
        Encryptor encryptor = new Encryptor();

        if (tweet.getText() != null) {
            return copyAndEncryptTweet(encryptor, tweet);
        }
        return null;
    }

}