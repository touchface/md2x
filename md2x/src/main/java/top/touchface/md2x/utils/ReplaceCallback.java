package top.touchface.md2x.utils;

/**
 * 正则表达式匹配到内容后的回调接口
 * 
 * @author touchface
 * date 2018-09-26 23:36
 */
public interface ReplaceCallback {
	/**
	 * 
	 * @param index 匹配结果的下标
	 * @param text string 匹配到的字符串
	 * @return 需要替换后的结果
	 */
	public String replace(int index,String text);
}
