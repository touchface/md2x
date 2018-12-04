package top.touchface.md2x.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配工具类
 * 
 * @author touchface
 * date 2018-09-26 21:47
 */
public class RegexUtils {

	/**
	 * 测试正则表达式与字符串是否相匹配
	 *
	 * @param regex   正则表达式
	 * @param string 测试的字符串
	 * @param flag   Pattern
	 * @return 匹配结果
	 */
	public static boolean test(String regex, int flag,String string) {

		Pattern pattern = Pattern.compile(regex, flag);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();

	}

	/**
	 * 测试正则表达式与字符串是否相匹配
	 * 
	 * @param regex 正则表达式
	 * @param string  测试的字符串
	 * @return 匹配结果
	 */
	public static boolean test(String regex, String string) {

		return test(regex,0, string);
	}


	/**
	 * if flag!=Parten.MULTILINE
	 * 获取匹配到的字符串以及子表达式匹配的结果
	 * cap[0]为匹配到的原字符串
	 * cap[0+]为子表达式匹配到的结果
	 * 只进行一次匹配
	 * 
	 * if flag==Parten.MULTILINE
	 * 获取字符串中所有相匹配的结果（不会返回子表达式的匹配结果）
	 * 
	 * 
	 * @param string 匹配的字符串
     * @param regex  正则表达式字符串
	 * @param flag   匹配模式
     *
	 * @return 匹配结果
	 */
	public static String[] match(String string, String regex, int flag) {
		Pattern pattern = Pattern.compile(regex, flag);
		return RegexUtils.match(string, pattern,flag);
	}

    /**
     * 匹配字符串，返回匹配结果
     * @param string 原字符串
     * @param regex 编译好的字符串
     * @param flag 匹配模式
     * @return 匹配结果
     */
    public static String[] match(String string, Pattern regex, int flag){
        Matcher matcher = regex.matcher(string);
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
            List<String> list= new ArrayList<>();
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
	 * 获取字符串中匹配到的字符串数组
	 * 
	 * @param string 进行匹配的字符串
	 * @param regex 正则表达式
	 * @return 匹配结果
	 */
	public static String[] match(String string, String regex) {
		return match(string, regex, 0);
	}
	/**
	 * 通过回调函数修改字符串
	 * 
	 * @param string 原字符串
	 * @param regex 正则表达式
	 * @param flag 匹配模式
	 * @param callback 回调函数
	 * @return 替换后的字符串
	 */
	public static String replaceAll(String string,String regex,int flag,int group,ReplaceCallback callback) {
		Pattern pattern=Pattern.compile(regex, flag);
		Matcher matcher=pattern.matcher(string);
		int index=0;// 匹配结果的下标
		StringBuffer sb=new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, callback.replace(index,matcher.group(group)));
			index++;
		}
		matcher.appendTail(sb);
		return sb.toString();
		
	}
	/**
	 * 修改匹配到的字符串
	 * 
	 * @param string 原字符串
	 * @param regex 正则表达式
	 * @param flag 匹配模式
	 * @param group 指定group
	 * @param replacement 修改字符串的回调函数
	 * @return 修改后的字符串
	 */
	public static String replaceAll(String string,String regex,int flag,int group,String replacement) {
		return RegexUtils.replaceAll(string, regex, flag, group, (index, text) -> replacement);
	}

}
