package core.db.repository;

import core.db.model.ConfigurationDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.security.auth.login.Configuration;

@RepositoryRestResource(collectionResourceRel = "configuration", path = "configuration")
public interface IConfigurationRepository extends MongoRepository<ConfigurationDto, String> {



}
