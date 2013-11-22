package grupo2.domain.bank;

import java.util.List;

/**
 * Repositorio de usuarios.
 */
public interface GroupCountRepo {


	public List<GroupCount> getAll();

	public GroupCount get(int bankId);
	
	public void add(GroupCount u);
	
}
