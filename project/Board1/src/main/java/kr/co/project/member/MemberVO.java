package kr.co.project.member;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberVO {
	private int no;
	private String email;
	private String pwd;
	private String name;
	private int gender;
	private String birthday;
	private String hp;
	private String zipcode;
	private String addr1;
	private String addr2;
	private Timestamp regdate;
	private int state;
}
