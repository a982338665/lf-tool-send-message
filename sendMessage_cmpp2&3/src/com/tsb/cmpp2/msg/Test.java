package com.tsb.cmpp2.msg;

import java.io.UnsupportedEncodingException;

import com.tsb.cmpp2.msg.util.MsgContainer;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
//		for (int i = 0; i < 10; i++) {
			//String s = "1.中国古代不合乐的称为诗，合乐的称为歌#!@@$$$，现代一般统称为诗歌。它按照一定的音节、韵律的要求，表现社会生活和2.人的精神世界。中国古代不合乐的称为诗，合乐的称为歌，现代一般统称为诗歌。它按照一&*!@#@($(()!)!定的音节3.韵律的要求，表现社会生活和人的精神世界。123456778end";
			String s = "作文学校由常州市中小学写作教学名教师工作室联盟承办。写作联盟由8个市、区级名教师工作室组成";
//			String s = "社团简介：“看得见的作文学校”是一个致力于让学生每次作文都取得看得见的进步的作文网校。作文学校由常州市中小学写作教学名教师工作室联盟承办。写作联盟由8个市、区级名教师工作室组成，涵盖了小学、初中1.2.48744545";
			//String s = "1.中国古代不合乐的称为诗，合乐的称为歌#!@@$$$，现代一般统称为诗歌。它按照一定的音节、韵律的要求，表现社会生活和2.人的精神世界。中国古代不合乐的称为诗，合乐的称为歌，现代一般统称为诗歌。它按照一&*!@#@($(()!)!定的音节3.韵律的要求，表现社会生活和人的精神世界。123456778end";
			System.out.println(s.length());
			MsgContainer.sendMsg(s, "13978004652");
			//MsgContainer.sendMsg(s, "13893368720");
			//MsgContainer.sendMsg(s, "15719334189");
		}

//	}

}
