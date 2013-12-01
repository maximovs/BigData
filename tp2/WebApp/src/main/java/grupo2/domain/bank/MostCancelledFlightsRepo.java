package grupo2.domain.bank;

import java.util.List;

/**
 * Repositorio de usuarios.
 */
public interface MostCancelledFlightsRepo {


	public List<String> getAll();

	public MostCancelledFlights get(String origin);
	
	public void add(MostCancelledFlights u);
	
}
