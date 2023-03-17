package TravelMgmtSys.ToutWayz_11.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="bookings")
public class Booking {

	@Id
	private String id;
    private String userEmail;
    private String fullName;
    private int guestSize;
    @NonNull
    @JsonFormat(pattern = "yyyy-mm-dd",shape = Shape.STRING)
    private Date bookAt;
    private String tourName;
    private int phone;
    
    
    
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getTourName() {
		return tourName;
	}
	public void setTourName(String tourName) {
		this.tourName = tourName;
	}
	public Date getBookAt() {
		return bookAt;
	}
	public void setBookAt(Date bookAt) {
		this.bookAt = bookAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getGuestSize() {
		return guestSize;
	}
	public void setGuestSize(int guestSize) {
		this.guestSize = guestSize;
	}
    
    
}
