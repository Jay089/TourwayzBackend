package TravelMgmtSys.ToutWayz_11.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import TravelMgmtSys.ToutWayz_11.Model.User;
import TravelMgmtSys.ToutWayz_11.Repository.UserRepository;

@Component
public class UserUtil {

    UserRepository userRepo;

    public UserUtil(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User getLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            String email = auth.getName();
            User user = userRepo.findByEmail(email);
            return user;
        }
        return null;


    }
}
