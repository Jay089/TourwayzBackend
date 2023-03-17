package TravelMgmtSys.ToutWayz_11.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import TravelMgmtSys.ToutWayz_11.Model.Review;



@Repository
public interface ReviewRepository extends MongoRepository<Review, String>{

	

}
