package au.com.telstra.simcardactivator.repository;

import au.com.telstra.simcardactivator.model.SimCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimCardRepository extends CrudRepository<SimCard, Long > {
}
