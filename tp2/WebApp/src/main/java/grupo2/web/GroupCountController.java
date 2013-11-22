package grupo2.web;


import grupo2.domain.bank.GroupCountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@Transactional
public class GroupCountController {
	private final GroupCountRepo groups;

	
	@Autowired
	public GroupCountController(GroupCountRepo groups) {
		this.groups = groups;
	}
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(groups.getAll());
		return mav;
	}
	
}
