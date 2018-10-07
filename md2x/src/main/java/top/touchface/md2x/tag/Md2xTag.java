package top.touchface.md2x.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import top.touchface.md2x.Md2x;
import top.touchface.md2x.entity.Options;

/**
 * JSP自定义标签，用于在JSP页面中直接使用本解析器
 * 
 * @author touchface
 * @date 2018-09-28 13:39
 */
public class Md2xTag extends SimpleTagSupport {

	private String value;// markdwon text
	private boolean gfm = true;// gfm grammar
	private boolean breaks = false;// gfm line breaks
	private boolean tables = true;// gfm tables
	private boolean pedantic = false;// pedantic Grammar
	private boolean headerIds = true;// automatic generation of id
	private String headerPrefix = "";// id prefix
	private String baseUrl = null;// base url for link
	private boolean mangle = true;
	private boolean sanitize = false;
	private boolean smartLists = false;
	private boolean smartypants = false;
	private boolean xhtml = false;

	private Options getOptions() {
		Options options = new Options();
		options.gfm = this.gfm;
		options.pedantic = this.pedantic;
		options.headerIds = this.headerIds;
		options.headerPrefix = this.headerPrefix;
		options.baseUrl = this.baseUrl;
		options.breaks = this.breaks;
		options.mangle = this.mangle;
		options.sanitize = this.sanitize;
		options.smartLists = this.smartLists;
		options.smartypants = this.smartypants;
		options.tables = this.tables;
		options.xhtml = this.xhtml;
		return options;
	}

	@Override
	public void doTag() throws JspException, IOException {

		// 创建容器获取标签体内容
		StringWriter sw = new StringWriter();
		String content = "";
		// 获取JspBody
		try {
			JspFragment jf = this.getJspBody();
			if (jf != null) {
				// 将标签提内容拷贝到sw中
				jf.invoke(sw);
				content = sw.toString();
				// 使用Md2x解析内容
				content = Md2x.parseToHtml(content, this.getOptions());

			} else if (value != null) {
				// 解析属性value中的markdwon文本
				content = Md2x.parseToHtml(value, this.getOptions());

			}
		} catch (RuntimeException e) {
			content+="<div style=\"font-family: Menlo, Monaco, Consolas;"
					+ "border:2px solid #000;padding:1rem;\">\n";
			content+= e.getMessage();
			content+="</div>\n";
			
		}
		// 获取JspWriter将内容输出到jsp页面
		JspWriter out = getJspContext().getOut();
		out.println(content);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setGfm(boolean gfm) {
		this.gfm = gfm;
	}

	public void setPedantic(boolean pedantic) {
		this.pedantic = pedantic;
	}

	public void setHeaderIds(boolean headerIds) {
		this.headerIds = headerIds;
	}

	public void setHeaderPrefix(String headerPrefix) {
		this.headerPrefix = headerPrefix;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setBreaks(boolean breaks) {
		this.breaks = breaks;
	}

	public void setTables(boolean tables) {
		this.tables = tables;
	}

	public void setMangle(boolean mangle) {
		this.mangle = mangle;
	}

	public void setSanitize(boolean sanitize) {
		this.sanitize = sanitize;
	}

	public void setSmartLists(boolean smartLists) {
		this.smartLists = smartLists;
	}

	public void setSmartypants(boolean smartypants) {
		this.smartypants = smartypants;
	}

	public void setXhtml(boolean xhtml) {
		this.xhtml = xhtml;
	}

}
