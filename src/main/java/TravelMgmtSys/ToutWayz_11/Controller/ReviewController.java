package TravelMgmtSys.ToutWayz_11.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TravelMgmtSys.ToutWayz_11.Model.Review;
import TravelMgmtSys.ToutWayz_11.Model.Tour;
import TravelMgmtSys.ToutWayz_11.Repository.ReviewRepository;
import TravelMgmtSys.ToutWayz_11.Repository.TourRepository;


@RestController
@RequestMapping("/tours")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private TourRepository tourRepo;

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> createReview(@PathVariable String id, @RequestBody Review review) {
   
    	reviewRepo.save(review);

        // Find the Tour object by ID and update it with the new review
        Optional<Tour> tourOptional = tourRepo.findById(id);
        
        if (tourOptional.isPresent()) {
            Tour tour = tourOptional.get();
            tour.getReviews().add(review);
            tourRepo.save(tour);
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(review);
    }

}

