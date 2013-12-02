	package grupo2.domain.bank;

import java.util.Date;

import grupo2.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "TotalFlights")
public class TotalFlights{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="date")
	private String date;
	@Column(name="total")
	private int total;
	@Column(name="cancelled")
	private int cancelled;
	
	
	TotalFlights() {
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}


	public int getCancelled() {
		return cancelled;
	}


	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	
}
