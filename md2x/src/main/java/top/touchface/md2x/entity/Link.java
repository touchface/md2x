package top.touchface.md2x.entity;

/**
 * Link
 * 
 * @author touchface
 * @date 2018-09-26 20:33
 */
public class Link {
	
	public String title;
	public String href;
	
	public Link() {
		super();
	}
	public Link(String title, String href) {
		super();
		this.title = title;
		this.href = href;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	@Override
	public String toString() {
		return "Link [title=" + title + ", href=" + href + "]";
	}
	
	
}
