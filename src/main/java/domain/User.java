package domain;

public class User {
	/*`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '����',
	  `username` VARCHAR(50) NOT NULL COMMENT '�û���',
	  `password` VARCHAR(50) NOT NULL COMMENT '����',
	  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '�ǳ�',
	  `age` INT(3) DEFAULT NULL COMMENT '���',
	  `sex` INT(1) DEFAULT NULL COMMENT '�Ա�0��1Ů��',
	  `phone` VARCHAR(11) DEFAULT NULL COMMENT '�ֻ�11λ',
	  `address` VARCHAR(200) DEFAULT NULL COMMENT '��ַ',
	  `isadmin` INT(1) NOT NULL COMMENT 'Ȩ�ޣ�0����Ա1��ͨ�û���',*/
	private int id;
	private String username;
	private String password;
	private String nickname;
	private int age;
	private int sex;
	private String phone;
	private String address;
	private int isadmin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getIsadmin() {
		return isadmin;
	}
	public void setIsadmin(int isadmin) {
		this.isadmin = isadmin;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
