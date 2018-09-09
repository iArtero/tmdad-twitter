package core.db.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "generatedTweets")
public class GeneratedTweetDto extends TweetJsonDto{

    @JsonIgnore
    private String idReferenced;
    public String plainText;
    public String searchedQuery;
    private int operation;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) { this.operation = operation; }

    public String getIdReferenced() {
        return idReferenced;
    }

    public void setIdReferenced(String idReferenced) {
        this.idReferenced = idReferenced;
    }

    public String getPlainText() {
        return plainText;
    }


    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public void setSearchedQuery(String searchedQuery) {
        this.searchedQuery = searchedQuery;
    }

    public String getSearchedQuery() {
        return searchedQuery;
    }

}
