	package grupo2.domain.bank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DateCancelled")
public class MostCancelledFlights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Hurricane")
	private String hurricane;
	@Column(name="Date")
	private String time;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCancellations() {
		return cancellations;
	}

	public void setCancellations(int cancellations) {
		this.cancellations = cancellations;
	}


}
