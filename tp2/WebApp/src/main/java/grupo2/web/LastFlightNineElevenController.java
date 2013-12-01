package grupo2.web;


import grupo2.domain.bank.LastFlightNineElevenRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Transactional
public class LastFlightNineElevenController {
	private final LastFlightNineElevenRepo lastFlights;

	
	@Autowired
	public LastFlightNineElevenController(LastFlightNineElevenRepo lastFlights) {
		this.lastFlights = lastFlights;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("lastFlightList",lastFlights.getAll());
		return mav;
	}
	
}
