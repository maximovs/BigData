package grupo2.web;


import java.util.ArrayList;
import java.util.List;

import grupo2.domain.bank.MostCancelledFlightsRepo;
import grupo2.domain.bank.TotalFlights;
import grupo2.domain.bank.TotalFlightsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Transactional
public class TotalFlightsController {
	private final TotalFlightsRepo totalFlights;

	
	@Autowired
	public TotalFlightsController(TotalFlightsRepo totalFlights) {
		this.totalFlights = totalFlights;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("flightsList",totalFlights.getAll());
		int total = 0;
		int cancelled = 0;
		List<String> dates = new ArrayList<String>();
		for(TotalFlights tf : totalFlights.getAll()){
			total += tf.getTotal();
			cancelled += tf.getCancelled();
			dates.add(tf.getDate());
		}
		mav.addObject("total",total);
		mav.addObject("totalcancelled",cancelled);
		mav.addObject("dates",dates);
		return mav;
	}
	
}
