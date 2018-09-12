package core.db.repository;

import core.db.model.SearchedWordDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "searchedWords", path = "searchedWords")
public interface ISearchedWordsRepository extends MongoRepository<SearchedWordDto, String> {



}
