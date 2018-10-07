package top.touchface.md2x.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 块级元素解析结果类
 * 
 * @author touchface
 * @date 2018-09-26 20:44
 */
public class Tokens {
	public LinkedList<Token> tokens;
	public Map<String,Link> links;
	public Tokens() {
		tokens=new LinkedList<Token>();
		links=new HashMap<String,Link>();
	}	
}
