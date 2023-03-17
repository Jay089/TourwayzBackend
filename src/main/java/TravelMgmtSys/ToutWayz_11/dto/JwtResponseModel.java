package TravelMgmtSys.ToutWayz_11.dto;

import org.springframework.data.mongodb.core.aggregation.VariableOperators;

import java.util.HashMap;

public class JwtResponseModel {

    private final String token;
    private final String userType;


    public JwtResponseModel(String token, String userType) {
        this.token = token;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public String getUserType() {
        return userType;
    }


    public HashMap<String,String> getData(){
        HashMap<String,String> res = new HashMap<>();
        res.put("token",this.token);
        res.put("userType",this.userType);
        return  res;
    }
}
