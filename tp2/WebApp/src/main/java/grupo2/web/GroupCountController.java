package grupo2.web;


import grupo2.domain.bank.GroupCount;
import grupo2.domain.bank.GroupCountRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String update() {
		List<GroupCount> list = groups.getAll();
		String resp = "[";

		for (GroupCount groupCount : list) {
			resp += "{\"name\":\"" + groupCount.getGroupId() + "\",\"data\":[" + groupCount.getAmmount() + "]},";
		}
		resp = resp.substring(0,resp.length()-1);
		return resp +"]";
	}
}
