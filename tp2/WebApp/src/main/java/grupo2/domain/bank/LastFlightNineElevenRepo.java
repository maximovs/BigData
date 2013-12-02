package grupo2.domain.bank;

import java.util.List;

/**
 * Repositorio de usuarios.
 */
public interface LastFlightNineElevenRepo {


	public List<LastFlightNineEleven> getAll();

	public LastFlightNineEleven get(String origin);
	
	public void add(LastFlightNineEleven u);
	
}
