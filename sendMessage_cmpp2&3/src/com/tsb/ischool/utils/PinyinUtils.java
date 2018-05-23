package com.tsb.ischool.utils;

import java.util.Vector;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@SuppressWarnings("unchecked")
public class PinyinUtils
{
  
public static Vector parseToPinying(String name)
  {
    name = name.toUpperCase();

    int count = 1;
    Vector v = new Vector();
    for (int i = 0; i < name.length(); i++) {
      Vector vv = new Vector();
      char c = name.charAt(i);
      String[] arr = parseToPinyin(c);
      if (arr == null) {
        vv.add(new Character(c).toString());
        count *= 1;
      } else {
        for (int j = 0; j < arr.length; j++) {
          vv.add(arr[j]);
        }
        count *= arr.length;
      }
      v.add(vv);
    }

    Vector temp = new Vector();
    for (int i = 0; i < count; i++) {
      temp.add(new StringBuilder(""));
    }

    for (int i = 0; i < v.size(); i++) {
      Vector vv = (Vector)v.get(i);
      int len = vv.size();
      for (int j = 0; j < len; j++) {
        String s = (String)vv.get(j);
        for (int k = j; k < temp.size(); k += len) {
          ((StringBuilder)temp.get(k)).append(s);
        }
      }
    }

    Vector ret = new Vector();
    for (int i = 0; i < temp.size(); i++) {
      ret.add(((StringBuilder)temp.get(i)).toString());
    }

    return ret;
  }

  public static String[] parseToPinyin(char c) {
    String[] ret = null;
    try {
      HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
      defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
      defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      ret = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
    } catch (BadHanyuPinyinOutputFormatCombination localBadHanyuPinyinOutputFormatCombination) {
    }
    return ret;
  }
  
  public static void main(String[] arg) {
    String s = "长ww88";
    //就取第一个就行
    Vector v = parseToPinying(s);
    System.out.println(v.get(0).toString());
    for (int i = 0; i < v.size(); i++)
      System.out.println(v.get(i).toString());
  }
}