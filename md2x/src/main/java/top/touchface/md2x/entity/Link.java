package top.touchface.md2x.entity;

/**
 * 链接类包含连接主题和链接地址
 * 
 * @author touchface
 * date 2018-09-26 20:33
 */
public class Link {
	
	public String title;
	public String href;

	public Link(String title, String href) {
		super();
		this.title = title;
		this.href = href;
	}
	@Override
	public String toString() {
		return "Link [title=" + title + ", href=" + href + "]";
	}
	
	
}
