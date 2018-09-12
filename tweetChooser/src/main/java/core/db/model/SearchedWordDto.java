package core.db.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "searchedWords")
public class SearchedWordDto {

    public String word;

    public String sessionId;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserFacebookId() {
        return userFacebookId;
    }

    public void setUserFacebookId(String userFacebookId) {
        this.userFacebookId = userFacebookId;
    }

    public String getUserFacebookName() {
        return userFacebookName;
    }

    public void setUserFacebookName(String userFacebookName) {
        this.userFacebookName = userFacebookName;
    }

    public String userFacebookId;

    public String userFacebookName;

}
