	package grupo2.domain.bank;

import java.util.Date;

import grupo2.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "DateCancelled")
public class MostCancelledFlights extends PersistentEntity{
	@Column(name="Hurricane")
	private String hurricane;
	@Column(name="Date")
	private Date time;
	@Column(name="Cancellations")
	private int cancellations;
	
	MostCancelledFlights() {
	}

	public String getHurricane() {
		return hurricane;
	}

	public void setHurricane(String hurricane) {
		this.hurricane = hurricane;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getCancellations() {
		return cancellations;
	}

	public void setCancellations(int cancellations) {
		this.cancellations = cancellations;
	}

	
}
