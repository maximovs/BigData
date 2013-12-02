package grupo2.domain.bank;

import grupo2.domain.AbstractHibernateRepo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HIbernateTotalFlightsRepo extends AbstractHibernateRepo implements	TotalFlightsRepo {

	@Autowired
	public HIbernateTotalFlightsRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<TotalFlights> getAll() {
		return this.find("from TotalFlights");
	}

	@Override
	public TotalFlights get(String origin) {
		return this.get(TotalFlights.class, origin);
	}

	@Override
	public void add(TotalFlights u) {
		this.save(u);
		
	}



}
