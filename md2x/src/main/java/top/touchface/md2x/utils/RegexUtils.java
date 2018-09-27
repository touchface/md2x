package top.touchface.md2x.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expression matching tool
 * 
 * @author touchface
 * @date 2018-09-26 21:47
 */
public class RegexUtils {

	/**
	 * Test whether regular expressions match strings.
	 *
	 * @param regex  
	 * @param string 
	 * @param flag   Parten
	 * @return 
	 */
	public static boolean test(String regex, int flag,String string) {

		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();

	}

	/**
	 * Test whether regular expressions match strings.
	 * 
	 * @param regex 
	 * @param string 
	 * @return 
	 */
	public static boolean test(String regex, String string) {

		return test(regex,0, string);
	}

	/**
	 * if flag!=Parten.MULTILINE
	 * Get strings that matched with regular expressions.
	 * cap[0] is the original string that matched.
	 * cap[1...] are strings matched by subexpression.
	 * 
	 * if flag==Parten.MULTILINE
	 * Get original strings that matched
	 * 
	 * 
	 * @param string 
	 * @param flag   
	 * @param regex  
	 * @return string array
	 */
	public static String[] match(String string, String regex, int flag) {
		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(string);
		String[] cap=null;
		if(flag!=Pattern.MULTILINE) {
			if(matcher.find()) {
				MatchResult mr = matcher.toMatchResult();
				cap = new String[mr.groupCount() + 1];
				for (int i = 0; i <mr.groupCount()+1; i++) {
					cap[i] = matcher.group(i);
				}
			}
			return cap;
		}else {
			List<String> list=new ArrayList<String>();
			while (matcher.find()) {
				list.add(matcher.group(0));
			}
			cap=new String[list.size()];
			for(int i=0;i<list.size();i++) {
				cap[i]=list.get(i);
			}
			
		}
		return cap;
	}
	
	/**
	 * Get strings that matched with regular expressions.
	 * 
	 * @param string
	 * @param regex
	 * @return
	 */
	public static String[] match(String string, String regex) {
		return match(string, regex, 0);
	}
	/**
	 * modifying the strings that matched
	 * 
	 * @param string
	 * @param regex
	 * @param flag
	 * @param callback
	 * @return
	 */
	public static String replaceAll(String string,String regex,int flag,int group,ReplaceCallback callback) {
		Pattern pattern=Pattern.compile(regex, flag);
		Matcher matcher=pattern.matcher(string);
		int index=0;
		StringBuffer sb=new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, callback.replace(index,matcher.group(group)));
		}
		matcher.appendTail(sb);
		return sb.toString();
		
	}
	/**
	 * modifying the strings that matched
	 * 
	 * @param string
	 * @param regex
	 * @param flag
	 * @param group
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String string,String regex,int flag,int group,String replacement) {
		return RegexUtils.replaceAll(string, regex, flag, group,new ReplaceCallback() {
			
			@Override
			public String replace(int index, String text) {
				
				return replacement;
			}
		});
	}

}
