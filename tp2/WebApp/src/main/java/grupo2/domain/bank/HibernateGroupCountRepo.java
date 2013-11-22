package grupo2.domain.bank;

import grupo2.domain.AbstractHibernateRepo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HibernateGroupCountRepo extends AbstractHibernateRepo implements
		GroupCountRepo {

	@Autowired
	public HibernateGroupCountRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<GroupCount> getAll() {
		return this.find("from GroupCount gc order by gc.ammount");
	}

	@Override
	public GroupCount get(final int bankId) {
		return this.get(GroupCount.class, bankId);
	}

	@Override
	public void add(GroupCount u) {
		this.save(u);
	}

}
