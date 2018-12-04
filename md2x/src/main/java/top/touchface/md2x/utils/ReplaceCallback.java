package top.touchface.md2x.utils;

/**
 * 正则表达式替换的回调函数
 * 
 * @author touchface
 * date 2018-09-26 23:36
 */
public interface ReplaceCallback {
	/**
	 * 
	 * @param index 匹配结果的下标
	 * @param text string 匹配到的字符串
	 * @return replacement 修改后的结果
	 */
	public String replace(int index,String text);
}
