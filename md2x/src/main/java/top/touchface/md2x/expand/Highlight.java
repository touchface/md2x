package top.touchface.md2x.expand;

/**
 * 语法高亮类，用于实现代码高亮的功能
 * （暂未实现，如有需要请继承本类进行实现，并在Options中进行设置来实现该功能）
 * 
 * @author touchface
 * @date 2018-09-26 20:47
 */
public abstract class Highlight {
	
	/**
	 * 对代码进行高亮
	 * 
	 * @param text code text
	 * @param lang language
	 * @return HTML text
	 */
	public abstract String highlight(String text,String lang);
}
