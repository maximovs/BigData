package grupo2.domain.bank;

import java.util.List;

/**
 * Repositorio de usuarios.
 */
public interface TotalFlightsRepo {


	public List<TotalFlights> getAll();

	public TotalFlights get(String date);
	
	public void add(TotalFlights u);
	
}
