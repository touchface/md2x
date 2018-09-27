package top.touchface.md2x.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The document that is analyzed and obtained includes token and links.
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
