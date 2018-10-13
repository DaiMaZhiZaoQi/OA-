package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import domain.Mail;
import domain.User;
import service.IMailService;
import service.IUserService;


@Controller
@RequestMapping("/mail")
public class MailController {
	
	@Autowired
	private IMailService service;
	@Autowired
	private IUserService ser;
	
	//д�ʼ�
	@RequestMapping("/mailWrite.action")
	public String writeMail(HttpSession session){
		List<User> users = ser.findAll();
		session.setAttribute("users", users);
		return "mailWrite";
	}
	//�ϴ�����
	@RequestMapping("/domailWrite.action")
	public String dowriteMail(MultipartFile attach ,Mail mail,HttpServletRequest request){
		mail.setIsread(1);
		mail.setCreateTime(new Date());
		mail.setIsDelete(0);
		String str = attach.getOriginalFilename();
		int i = str.lastIndexOf(".");
		String attachment = UUID.randomUUID().toString()+str.substring(i);
		mail.setAttachment(attach.getOriginalFilename());
		//�ϴ�����������WebApp/uploads�ļ�����
		String savepath = request.getServletContext().getRealPath("/uploads/"+attachment);
		File file = new File(savepath);
		//ʹ��transferTo�����ϴ���ָ��λ��
		try {
			attach.transferTo(file);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.save(mail);
		return "redirect:/mail/mailWrite.action";
	}
	
	//���ʼ�
	@RequestMapping("/receive.action")
	public String mailReceive(HttpSession session){
		User user = (User) session.getAttribute("sessionUser");
		List<Mail> findmail = service.findByToId(user);
		List<Mail> mails = new ArrayList<Mail>();
		for (Mail mail : findmail) {
			if(mail.getContent().length()>5){
				mail.setContent(mail.getContent().substring(0, 5)+"...");
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
			String format = dateFormat.format(mail.getCreateTime());
			mail.setCreate(format);
			if(mail.getIsDelete()==0){
				mails.add(mail);
			}
		}
		session.setAttribute("mails", mails);
		return "receive";
	}
	//ɾ���ʼ�
	@RequestMapping("/garage.action")
	public String mailGarage(HttpSession session){
		User user = (User) session.getAttribute("sessionUser");
		List<Mail> findmail = service.findByToId(user);
		List<Mail> mails = new ArrayList<Mail>();
		for (Mail mail : findmail) {
			if(mail.getContent().length()>5){
				mail.setContent(mail.getContent().substring(0, 5)+"...");
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
			String format = dateFormat.format(mail.getCreateTime());
			mail.setCreate(format);
			if(mail.getIsDelete()==1){
				mails.add(mail);
			}
		}
		session.setAttribute("mails", mails);
		return "garage";
	}
	@RequestMapping("/deletemail.action")
	public String deleteMail(HttpServletRequest request){
		String str = request.getParameter("id");
		int id =0;
		if(str!=null){
			id = Integer.parseInt(str);
		}
		Mail mail = new Mail();
		mail.setId(id);
		Mail mail2 = service.findById(mail);
		mail2.setIsDelete(1);
		service.modify(mail2);
		return "redirect:/mail/receive.action";
	}
	
	//�Ķ��ʼ�
	@RequestMapping("/readmail.action")
	public String readMail(HttpServletRequest request,HttpSession session){
		String str = request.getParameter("id");
		int id =0;
		if(str!=null){
			id = Integer.parseInt(str);
		}
		Mail mail = new Mail();
		mail.setId(id);
		Mail mail2 = service.findById(mail);
		mail2.setIsread(0);
		service.modify(mail2);
		return "forward:/mail/read.action";
	}
	
	@RequestMapping("/read.action")
	public String read(HttpServletRequest request,HttpSession session){
		String str = request.getParameter("id");
		int id =0;
		if(str!=null){
			id = Integer.parseInt(str);
		}
		Mail mail = new Mail();
		mail.setId(id);
		Mail mail2 = service.findById(mail);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		String format = dateFormat.format(mail2.getCreateTime());
		mail2.setCreate(format);
		User user = new User();
		user.setId(mail2.getFromID());
		String fromname = ser.findById(user).getNickname();
		mail2.setFromName(fromname);
		session.setAttribute("mail", mail2);
		return "readMail";
	}
	
	//�����ʼ��еĸ���
	@RequestMapping("/download.action")
	public ResponseEntity<byte[]> doDownload(HttpServletRequest request) throws IOException{
		
		//һ������ݿ���ж�ȡ��Ӧ�ļ�λ�ã������ݶ�Ϊ����d:/1.jpg��Ӧ�ļ�
		String attachment = request.getParameter("attachment");
		String location = "E:/java����/Workspace(work2)/day10_ssm_test/src/main/webapp/uploads/"+attachment;
		//1.������Ӧͷ��Ϣ:filename���������������غ���ļ�����
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachement;filename="+URLEncoder.encode(attachment, "utf-8"));
		//2.������Ӧ״̬��200
		HttpStatus statusCode = HttpStatus.OK;
		//3.����������,��ȡ�ļ�
		FileInputStream fis = new FileInputStream(location);
		//�����ֽ�����
		byte[] bs = new byte[fis.available()];
		fis.read(bs);
		//4.�ر���
		fis.close();
		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(bs, headers, statusCode);
		return entity;
		
	}
	
	//��ԭɾ���ʼ�
	@RequestMapping("/rollback.action")
	public String rollbackDelete(HttpServletRequest request){
		String str = request.getParameter("id");
		int id =0;
		if(str!=null){
			id = Integer.parseInt(str);
		}
		Mail mail = new Mail();
		mail.setId(id);
		Mail mail2 = service.findById(mail);
		mail2.setIsDelete(0);
		service.modify(mail2);
		return "forward:/mail/garage.action";
	}
	//����ɾ���ʼ�
	@RequestMapping("/reallydeletemail.action")
	public String reallyDeleteMail(HttpServletRequest request){
		String str = request.getParameter("id");
		int id =0;
		if(str!=null){
			id = Integer.parseInt(str);
		}
		Mail mail = new Mail();
		mail.setId(id);
		service.remove(mail);
		return "forward:/mail/garage.action";
	}
	
	
	
	
}
