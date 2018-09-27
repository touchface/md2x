package top.touchface.md2x.entity;

import java.util.List;
/**
 * Token for markdown
 * 
 * @author touchface
 * @date 2018-09-26 20:41
 */
public class Token {
	public Type type;
	public String text;
	
	public String title;
	public String href;
	
	public int depth;
	
	public boolean ordered;
	public String start;
	public boolean task;
	public boolean checked;
	public boolean loose;
	
	public List<String> header;
	public List<String> align;
	public List<List<String>> cells;
	
	public String lang;
	public boolean escaped=false;
}
