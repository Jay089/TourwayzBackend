package TravelMgmtSys.ToutWayz_11.Controller;


import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mongodb.BasicDBObject;

import TravelMgmtSys.ToutWayz_11.Model.Tour;
import TravelMgmtSys.ToutWayz_11.Model.User;
import TravelMgmtSys.ToutWayz_11.Repository.TourRepository;
import TravelMgmtSys.ToutWayz_11.Repository.UserRepository;
import TravelMgmtSys.ToutWayz_11.utils.UserUtil;
import jakarta.websocket.server.PathParam;


@RestController
@RequestMapping("/tours")
@CrossOrigin
public class TourController {

	@Autowired
	private TourRepository tourRepo;
	
    @Autowired
    private UserRepository userRepo;
	
	@Autowired
	private MongoTemplate mongotemp;
	

	// Create Tour
	@PostMapping("/")
	public ResponseEntity<String> createTour(@RequestBody Tour tour) {

		try {
            UserUtil util = new UserUtil(userRepo);
            User user = util.getLoggedInUser();
            if(user == null){
                // throw error that user is not logged in else use the user
            }
			tourRepo.save(tour);

			String responseBody = "Successfully created";
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	// Update Tour

	@PutMapping("/{id}")
	public ResponseEntity<?> updateTour(@PathVariable("id") String id, @RequestBody Tour tour) {

		try {
			Optional<Tour> tourData = tourRepo.findById(id);
			if (tourData.isPresent()) {
				Tour newTour = tourData.get();
				newTour.setTitle(tour.getTitle());
				newTour.setCity(tour.getCity());
				newTour.setAddress(tour.getAddress());
				newTour.setDistance(tour.getDistance());
				newTour.setPrice(tour.getPrice());
				newTour.setMaxGroupSize(tour.getMaxGroupSize());
				newTour.setDesc(tour.getDesc());
				newTour.setPhoto(tour.getPhoto());
				newTour.setFeatured(tour.isFeatured());

				tourRepo.save(newTour);

			}
			return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// Delete Tour

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTour(@PathVariable("id") String id) {

		try {
			tourRepo.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	// Get a Single Your

	@GetMapping("/{id}")
	public ResponseEntity<?> getSingleTour(@PathVariable("id") String id) {

		try {
			Optional<Tour> singleTour = tourRepo.findById(id);

			return new ResponseEntity<>(singleTour.get(), HttpStatus.OK);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	
	
//	@GetMapping("/{id}")
//	public ResponseEntity<?> getTour(@PathVariable String id) {
//	    Optional<Tour> tourOptional = tourRepo.findById(id);
//	    if (!tourOptional.isPresent()) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour not found");
//	    } else {
//	        Tour tour = tourOptional.get();
//	        // Populate reviews for tour
//	        mongotemp.getConverter().write(tour, new BasicDBObject());
//	        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
//	        Tour tourWithReviews = mongotemp.findOne(query, Tour.class, "tours");
//
//	        return ResponseEntity.status(HttpStatus.OK).body(tourWithReviews);
//	    }
//	}

	// Get All Tour

	@GetMapping("/")
    public ResponseEntity<?> getAllTour(){

        List<Tour> tour = tourRepo.findAll();

        if(tour.size() > 0){
            return  new ResponseEntity<List<Tour>>(tour , HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No tours availabel" , HttpStatus.NOT_FOUND);
        }

    }

	
	
	//get Tour By Search
	
	@GetMapping("/search/getTourBySearch")
	List<Tour> getTourBySearch(@PathParam(value="city") String city,@PathParam(value="distance") int distance,@PathParam(value="maxGroupSize") int maxGroupSize){
		
		Query qry = new Query();
		qry.addCriteria(Criteria.where("city").is(city).and("distance").gte(distance).and("maxGroupSize").gte(maxGroupSize));
		return mongotemp.find(qry,Tour.class);
	}
	
	@GetMapping("/search/getFeatured")
	List<Tour> getTourBySearch(){
		
		Query qry = new Query();
		Object featured = true;
		qry.addCriteria(Criteria.where("featured").is(featured));
		return mongotemp.find(qry,Tour.class);
	}
	
	//get Tour Count
	//do it later

}
