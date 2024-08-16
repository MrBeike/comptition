package pbc.chi.kjk.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import pbc.chi.kjk.dao.ImportExcelDao;
import pbc.chi.kjk.pojo.Question;
import pbc.chi.kjk.service.QuestionService;

@Controller
public class WebConctroller {


	@Autowired
    private ImportExcelDao importExcelDao;
	@Autowired
    private QuestionService questionService;
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(Model model) {
		return "index";
	}
	
	
	/**
	 * 背景页
	 * @param model
	 * @return
	 */
	@RequestMapping("welcome")
	public String welcome(Model model) {
		return "welcome";
	}
	
	
	/**
	 * 管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping("manage")
	public String manage(Model model) {
		return "manage";
	}
	
	
	/**
	 * 转盘页面页面
	 * @param model
	 * @return
	 */
	@RequestMapping("turnselect")
	public String turnselect(Model model) {
		return "turnselect";
	}
	
	/**
	 * 抢答环节页面
	 * @param model
	 * @return
	 */
	@RequestMapping("robselect")
	public String robselect(Model model) {
		return "robselect";
	}
	
	/**
	 * 跳转至风险分类页面
	 * @param model
	 * @return
	 */
	@RequestMapping("riskselect")
	public String riskselect(Model model) {
		return "riskselect";
	}
	
	/**
	 * 跳转至抽题页面	
	 * @param flag
	 * @param model
	 * @return
	 */
	@RequestMapping("select/{flag}")
	public String selecttype(@PathVariable Integer flag,Model model) {
		String qutype="";
		Integer selectednum ;
		if(flag==1) {
			qutype="必答题";
		}else if(flag==2) {
			qutype="抢答题";
		}else if(flag==3) {
			qutype="低风险题";
		}else if(flag==4) {
			qutype="中风险题";
		}else {
			qutype="高风险题";
		}
		List<Question> qlist = questionService.getQuestionList(flag,1);
		if(qlist == null || qlist.size() == 0 ) {
			selectednum = 0;
        }else {
        	selectednum = qlist.size();
        }
		
		model.addAttribute("selectednum", selectednum);
		model.addAttribute("qutype", qutype);
		model.addAttribute("flag", flag);
		return "select";
	}
	
	/**
	 * 试题展示页面
	 * @param model
	 * @return
	 */
	@RequestMapping("questionshow")
	public String questionshow(HttpServletRequest request,Model model) {
		String qutype="";
		String que_num = request.getParameter("id");
		Integer flag = Integer.parseInt(request.getParameter("flag"));
		if(flag==1) {
			qutype="必答题";
		}else if(flag==2) {
			qutype="抢答题";
		}else if(flag==3) {
			qutype="低风险题";
		}else if(flag==4) {
			qutype="中风险题";
		}else {
			qutype="高风险题";
		}
		Question qu = questionService.findQuestion(flag,Integer.parseInt(que_num));
		//将题库中该题出除
		questionService.update(qu);
		model.addAttribute("qu", qu);
		model.addAttribute("qutype", qutype);
		model.addAttribute("flag", flag);
		return "questionshow";
	}
	
	
	
	/**
	 * 抢答题顺序出题展示页面
	 * @param model
	 * @return
	 */
	@RequestMapping("robquestionshow")
	public String robquestionshow(HttpServletRequest request,Model model) {
		String qutype="";
		Integer selectednum ;
		String pre_title="";
		Integer flag = Integer.parseInt(request.getParameter("flag"));
		if(flag==1) {
			qutype="必答题";
		}else if(flag==2) {
			qutype="抢答题";
		}else if(flag==3) {
			qutype="低风险题";
		}else if(flag==4) {
			qutype="中风险题";
		}else {
			qutype="高风险题";
		}
		Question qu = new Question();
		List<Question> qlist = questionService.getQuestionList(flag,0);
		if(qlist.size()>0) {
			qu = qlist.get(0);		
		}	
		
		//计算已答多少题
		List<Question> qlist2 = questionService.getQuestionList(flag,1);
		if(qlist2 == null || qlist2.size() == 0 ) {
			selectednum = 0;
        }else {
        	selectednum = qlist2.size();
        }			
		//判断该题是抢答题还是加赛题
		int q_num = qu.getQue_num();
		if(q_num>12) {
			q_num = q_num-12;
			pre_title = "题号："+q_num;
		}else {
			pre_title = "题号："+q_num;
		}
		//将题库中该题出除
		questionService.update(qu);
		model.addAttribute("qu", qu);
		model.addAttribute("qutype", qutype);
		model.addAttribute("pre_title", pre_title);
		model.addAttribute("flag", flag);
		model.addAttribute("selectednum", selectednum);
		return "robquestionshow";
	}
	
	/**
	 * 导入试题库
	 * @param excel
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="importExcel",method=RequestMethod.POST)
	@ResponseBody
    public String importExcel(MultipartFile excel,HttpServletRequest req,HttpServletResponse resp) {
		List<Question> list = importExcelDao.importExcelWithSimple(excel, req, resp);
        if(list == null || list.size() == 0 ) {
            return "fail";
        }
              
        //批量插入list到数据库
        for(int i=0;i<list.size();i++) {
        	System.out.println((i+1)+"==="+list.get(i).getQue_num()+"---"+list.get(i).getQue_title()+"---"+list.get(i).getFenzhi());
        	questionService.add(list.get(i));
        }
        return "success";
    }

	/**
	 * 判断题库中是否有试题
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="isHasQuestion",method=RequestMethod.POST)
	@ResponseBody
    public String isHasQuestion(HttpServletRequest req,HttpServletResponse resp) {
		String msg = "";
		Integer flag = Integer.parseInt(req.getParameter("flag"));
		List<Question> qlist = questionService.getQuestionList(flag,0);
		if(qlist!=null && qlist.size()>0) {
			msg = "success" ;
		}else {
			msg = "error";
		}
        return msg;
    }
	
	/**
	 * 每次获取最新的题库试题，出除已被抽到的试题
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="getQuestionListExselect",method=RequestMethod.POST)
	@ResponseBody
    public List<Question> getQuestionListExselect(HttpServletRequest req,HttpServletResponse resp) {
		Integer flag = Integer.parseInt(req.getParameter("flag"));
		System.out.println("flag="+flag);
		List<Question> qlist = questionService.getQuestionList(flag,0);		
        return qlist;
    }
	
	/**
	 * 系统初始化数据
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="initdata",method=RequestMethod.POST)
	@ResponseBody
    public String initdata(HttpServletRequest req,HttpServletResponse resp) {
		String msg = "";
		List<Question> qlist = questionService.getQuestionAllList();		
		if(qlist!=null && qlist.size()>0) {
			for(Question q:qlist) {
				questionService.initdata(q);
			}
			msg = "success" ;
		}else {
			msg = "error";
		}
        return msg;
    }
	
	/**
	 * 删除系统数据
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="deldata",method=RequestMethod.POST)
	@ResponseBody
    public String deldata(HttpServletRequest req,HttpServletResponse resp) {
		questionService.deldata();
		String	msg = "success" ;
        return msg;
    }
	
}
