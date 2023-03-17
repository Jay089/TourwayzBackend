package TravelMgmtSys.ToutWayz_11.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TravelMgmtSys.ToutWayz_11.Config.JwtTokenUtil;
import TravelMgmtSys.ToutWayz_11.Model.User;
import TravelMgmtSys.ToutWayz_11.Repository.UserRepository;
import TravelMgmtSys.ToutWayz_11.dto.JwtResponseModel;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private PasswordEncoder passwordEncoder;

	
	

	// Create User
	@PostMapping("/users")

	public ResponseEntity<String> createUser(@RequestBody User user) {

		try {

			userRepo.save(user);

			String responseBody = "Successfully created";
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	// Update User

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody User user) {

		try {
			Optional<User> userData = userRepo.findById(id);
			if (userData.isPresent()) {

				User newUser = userData.get();
				newUser.setUsername(user.getUsername());
				newUser.setEmail(user.getEmail());
				newUser.setPassword(user.getPassword());
				newUser.setPhoto(user.getPhoto());

				userRepo.save(newUser);

			}
			return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	// Get a Single user

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getSingleTour(@PathVariable("id") String id) {

		try {
			Optional<User> singleUser = userRepo.findById(id);

			return new ResponseEntity<>(singleUser.get(), HttpStatus.OK);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	// Get All Users

	@GetMapping("/users/")
	public ResponseEntity<?> getAllUsers() {

		List<User> users = userRepo.findAll();

		if (users.size() > 0) {
			return new ResponseEntity<List<User>>(users, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No tours availabel", HttpStatus.NOT_FOUND);
		}

	}
	
	// Register User
	@PostMapping("/register")
	public ResponseEntity<?> RegisterUser(@RequestBody User user,HttpServletRequest request) {

		try {

			userRepo.save(user);

			final String token = jwtTokenUtil.generateToken(user.getEmail(),request);
            JwtResponseModel jwtResponseModel = new JwtResponseModel(token,"abcd");
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponseModel.getData());
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User userReq, HttpServletRequest request,HttpServletResponse response) {

	    User user = userRepo.findByEmail(userReq.getEmail());

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.OK).body("user not register");
	    } else {

            Boolean checkPass = userReq.getPassword().equals(user.getPassword());
            if(checkPass){
                final String token = jwtTokenUtil.generateToken(user.getEmail(),request);
                JwtResponseModel jwtResponseModel = new JwtResponseModel(token,"abcd");
                int expiresIn = 60 * 60 * 10000;
                Cookie cookie = new Cookie("accessToken", token);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(expiresIn);

                response.addCookie(cookie);
                return ResponseEntity.status(HttpStatus.OK).body(user);

	        } else {
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
	             }
	    }
	}

	

}
