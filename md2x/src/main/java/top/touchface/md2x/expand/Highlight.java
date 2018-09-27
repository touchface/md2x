package top.touchface.md2x.expand;

/**
 * This abstract class is used to realize the 
 * highlight function of the code block in Markdown.
 * 
 * @author touchface
 * @date 2018-09-26 20:47
 */
public abstract class Highlight {
	
	/**
	 * Highlight the code text and output the HTML text.
	 * @param text code text
	 * @param lang language
	 * @return HTML text
	 */
	public abstract String highlight(String text,String lang);
}
