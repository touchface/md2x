package top.touchface.md2x.tag;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import top.touchface.md2x.Md2x;

/**
 * JSP自定义标签，使用Md2x的默认配置来解析Markdown
 *
 * @author touchface
 * date 2018-09-28 13:39
 */
public class Md2xTag extends SimpleTagSupport {

	private Md2x md2x;// Md2x对象
    private String value;// 需要解析的文本

	public Md2xTag(){
	    md2x=new Md2x();
    }

	@Override
	public void doTag() throws IOException {

		// 创建容器获取标签体内容
		StringWriter sw = new StringWriter();
		String content = "";
		// 获取JspBody
		try {
			JspFragment jf = this.getJspBody();
			if (jf != null) {
				// 将标签体中的内容拷贝到sw中
				jf.invoke(sw);
				content = sw.toString();
				// 使用Md2x解析内容
				content = md2x.parse(content);

			} else if (value != null) {
				// 解析属性value中的markdwon文本
                content = md2x.parse(content);

			}
		} catch (Exception e) {
			content+="<div style=\"font-family: Menlo, Monaco, Consolas;"
					+ "border:2px solid #000;padding:1rem;\">\n";
			content+= e.getMessage();
			content+="<div>md2x:<a href='https://github.com/touchface/md2x'>https://github.com/touchface/md2x</a></div>\n";
			content+="</div>\n";
			
		}
		// 获取JspWriter将内容输出到jsp页面
		JspWriter out = getJspContext().getOut();
		out.println(content);
	}
	public void setValue(String value) {
		this.value = value;
	}

}
