package grupo2.domain.bank;

import grupo2.domain.AbstractHibernateRepo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HibernateLastFlightNineElevenRepo extends AbstractHibernateRepo implements	LastFlightNineElevenRepo {

	@Autowired
	public HibernateLastFlightNineElevenRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<LastFlightNineEleven> getAll() {
		return this.find("from LastFlightNineEleven");
	}

	@Override
	public LastFlightNineEleven get(String origin) {
		return this.get(LastFlightNineEleven.class, origin);
	}

	@Override
	public void add(LastFlightNineEleven u) {
		this.save(u);
		
	}



}
