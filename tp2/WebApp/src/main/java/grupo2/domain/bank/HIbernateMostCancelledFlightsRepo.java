package grupo2.domain.bank;

import grupo2.domain.AbstractHibernateRepo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class HIbernateMostCancelledFlightsRepo extends AbstractHibernateRepo implements	MostCancelledFlightsRepo {

	@Autowired
	public HIbernateMostCancelledFlightsRepo(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public List<MostCancelledFlights> getAll() {
		return this.find("from MostCancelledFlights");
	}

	@Override
	public MostCancelledFlights get(String origin) {
		return this.get(MostCancelledFlights.class, origin);
	}

	@Override
	public void add(MostCancelledFlights u) {
		this.save(u);
		
	}



}
