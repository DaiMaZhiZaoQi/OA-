package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import domain.User;
import service.IUserService;
import util.Constants;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService service;
	
	//��¼ģ��
	@RequestMapping("/login.do")
	public String toLogin(){
		return "login";
	}
	
	@RequestMapping("/dologin.do")
	@ResponseBody
	public String doLogin(User user,HttpSession session){
		User finduser = service.findByName(user);
		if(finduser!=null){
			if(finduser.getPassword().equals(user.getPassword())){
				session.setAttribute("sessionUser", finduser);
				return Constants.LOGIN_SUCCESS;
			}
		}
		return Constants.LOGIN_FAIL;
		
	}
	
	@RequestMapping("/index.do")
	public String toIndex(){
		return "index";
	}
	//ע��
	@RequestMapping("/logout.do")
	public String logout(){
		return "login";
	}
	
	//��ת���޸��û�ҳ��
	@RequestMapping("/editData.action")
	public String editDate(User user,HttpSession session){
		User finduser = service.findById(user);
		session.setAttribute("sessionUser", finduser);
		return "editUser";
	}
	//�޸��û�
	@RequestMapping("/doeditData.action")
	public String doeditDate(User user,HttpSession session){
		service.modify(user);
		User finduser = service.findById(user);
		session.setAttribute("sessionUser", finduser);
		return "index";
	}
	
	//Ȩ�޹���ģ��
	
	//�����˻�
	@RequestMapping("/singleAccountData.action")
	public String toAccountData(){
		return "account";
	}
	//�޸ĸ����˻�
	@RequestMapping("/editAccountData.action")
	public String doAccountData(User user,HttpSession session){
		User user2 = service.findById(user);
		user2.setUsername(user.getUsername());
		user2.setPassword(user.getPassword());
		service.modify(user2);
		session.setAttribute("sessionUser", user2);
		return "redirect:/user/singleAccountData.action";
	}
	
	//�鿴�����˻���Ϣ
	@RequestMapping("/findAccountData.action")
	public String toFindAccountData(HttpSession session){
		List<User> users = service.findAll();
		session.setAttribute("list", users);
		return "findAccount";
	}
	//����˻�
	@RequestMapping("/addAccountData.action")
	public String toAddAccountData(){
		return "addAccount";
	}
	
	@RequestMapping("/doaddaccount.action")
	public String doAddAccountData(User user){
		service.save(user);
		return "redirect:/user/findAccountData.action";
	}
	
	
	
	
	
	

}
