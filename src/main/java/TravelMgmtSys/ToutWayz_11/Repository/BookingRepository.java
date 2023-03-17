package TravelMgmtSys.ToutWayz_11.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import TravelMgmtSys.ToutWayz_11.Model.Booking;

import java.awt.print.Book;
import java.util.List;


@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserEmail(String email);
}
