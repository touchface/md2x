package top.touchface.md2x.utils;

/**
 * replace callback
 * 
 * @author touchface
 * @date 2018-09-26 23:36
 */
public interface ReplaceCallback {
	/**
	 * 
	 * @param index
	 * @param text string that matched
	 * @return replacement
	 */
	public String replace(int index,String text);
}
