package TravelMgmtSys.ToutWayz_11.Controller;

import java.util.List;
import java.util.Optional;

import TravelMgmtSys.ToutWayz_11.Model.User;
import TravelMgmtSys.ToutWayz_11.Repository.UserRepository;
import TravelMgmtSys.ToutWayz_11.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import TravelMgmtSys.ToutWayz_11.Model.Booking;
import TravelMgmtSys.ToutWayz_11.Repository.BookingRepository;



@RestController
@RequestMapping("/booking")
@CrossOrigin
public class BookingController {

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private UserRepository userRepo;
	// Create Booking
	@PostMapping("/")
	public ResponseEntity<String> createTour(@RequestBody Booking book) {

		try {

			bookingRepo.save(book);

			String responseBody = "{\"message\": \"Successfully created\"}";
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	// get single booking
	@GetMapping("/{id}")
	public ResponseEntity<?> getSingleBooking(@PathVariable("id") String id) {

		try {
			Optional<Booking> singleBooking = bookingRepo.findById(id);

			return new ResponseEntity<>(singleBooking.get(), HttpStatus.OK);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	// Get All Bookings

	@GetMapping("/")
    public ResponseEntity<?> getAllbookings(){

        List<Booking> book = bookingRepo.findAll();

        if(book.size() > 0){
            return  new ResponseEntity<List<Booking>>(book , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No tours availabel" , HttpStatus.NOT_FOUND);
        }

    }
	@GetMapping("/all/{email}")
	public ResponseEntity<?> getMybookings(@PathVariable("email") String email){

		List<Booking> book = bookingRepo.findByUserEmail(email);

		if(book.size() > 0){
			return  new ResponseEntity<List<Booking>>(book , HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No tours available" , HttpStatus.NOT_FOUND);
		}
	}
}