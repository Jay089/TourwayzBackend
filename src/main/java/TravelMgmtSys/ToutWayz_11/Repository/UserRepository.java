package TravelMgmtSys.ToutWayz_11.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import TravelMgmtSys.ToutWayz_11.Model.User;



@Repository
public interface UserRepository extends MongoRepository<User, String>{

	User findByEmail(String email);

}
