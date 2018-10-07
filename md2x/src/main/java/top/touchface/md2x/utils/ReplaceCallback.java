package top.touchface.md2x.utils;

/**
 * 正则表达式替换的回调函数
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
