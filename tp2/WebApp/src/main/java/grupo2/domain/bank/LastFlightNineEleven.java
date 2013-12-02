	package grupo2.domain.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TimeLastFlightNineEleven")
public class LastFlightNineEleven {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Origin")
	private String origin;
	@Column(name="Time")
	private String time;

	LastFlightNineEleven() {
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}





}
