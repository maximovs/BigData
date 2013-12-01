package grupo2.web;


import grupo2.domain.bank.MostCancelledFlightsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Transactional
public class MostCancelledFlightsController {
	private final MostCancelledFlightsRepo mostCancelled;

	
	@Autowired
	public MostCancelledFlightsController(MostCancelledFlightsRepo mostCancelled) {
		this.mostCancelled = mostCancelled;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mostCancelledList",mostCancelled.getAll());
		return mav;
	}
	
}
