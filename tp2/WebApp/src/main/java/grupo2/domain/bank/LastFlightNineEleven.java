	package grupo2.domain.bank;

import java.util.Date;

import grupo2.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "TimeLastFlightNineEleven")
public class LastFlightNineEleven extends PersistentEntity{
	@Column(name="Origin")
	private String origin;
	@Column(name="Time")
	private Date time;
	
	LastFlightNineEleven() {
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	


		
}
