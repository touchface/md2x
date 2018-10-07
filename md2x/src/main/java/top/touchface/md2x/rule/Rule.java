package top.touchface.md2x.rule;
import top.touchface.md2x.utils.RegexUtils;

/**
 * 匹配规则类
 *  
 * @author touchface
 * @date 2018-09-26 21:32
 */
public class Rule {

	private String regex;// 规则对应正则表达式
	private int flag;//规则的解析模式（Parten类里对应的模式）
	public Rule(Rule rule) {
		this.regex =new String(rule.regex);
		this.flag=rule.getFlag();
	}
	public Rule(String rgex) {
		this.regex = rgex;
		setFlag(0);
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	/**
	 *	匹配正则表达式匹配到的第一条结果，并返回匹配结果的数组
	 * 
	 *	cap[0] 为匹配到的字符串 cap[0+] 为子表达式匹配到的结果
	 * 
	 * @param src 需要进行匹配的字符串
	 * @return 匹配结果数组
	 */
	public String[] exec(String src) {
		return RegexUtils.match(src, regex, flag);
	}

	/**
	 *	将表达式中的字符替换为指定内容 code/g 替换所有内容；code只替换第一个
	 * 
	 * @param code        需要进行替换的字符串
	 * @param replacement 替换的内容
	 * @return 修改后的规则
	 */
	public Rule replace(String code, String replacement) {
		
		replacement=replacement.replaceAll("(^|[^\\[])\\^","$1");
		if(code.endsWith("/g")) {
			this.regex = this.regex.replace(code.substring(0,code.length()-2), replacement);
		}else {
			
			int index=this.regex.indexOf(code);
			String begin=this.regex.substring(0, index);
			String end=this.regex.substring(index+code.length(), this.regex.length());
			this.regex=begin+replacement+end;
		}
		
		return this;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
}
