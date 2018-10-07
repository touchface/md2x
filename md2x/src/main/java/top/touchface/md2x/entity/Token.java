package top.touchface.md2x.entity;

import java.util.List;
/**
 * Markdown标记类
 * 
 * @author touchface
 * @date 2018-09-26 20:41
 */
public class Token {
	public Type type;//标记类型
	public String text;//文本
	
	public String title;//标题
	public String href;//路径
	
	public int depth;//分级标题的等级
	
	public boolean ordered;//是否为有序列表
	public String start;//列表的开始下标
	
	public boolean task;//待办列表
	public boolean checked;//是否选中
	public boolean loose;
	
	public List<String> header;//表头
	public List<String> align;//表格每一列的文本居中方式
	public List<List<String>> cells;//表格的表格
	
	public String lang;//代码语言前缀
	public boolean escaped=false;
}
