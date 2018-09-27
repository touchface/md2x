package top.touchface.md2x.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import top.touchface.md2x.Md2x;

public class Md2xTag extends SimpleTagSupport{
	private Md2x md2x=new Md2x();
	
	private String value;//接收markdown文本
	
	public void setValue(String value) {
		this.value=value;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		
		//创建容器获取标签体内容
		StringWriter sw=new StringWriter();
		String content="";
		//获取JspBody
		JspFragment jf=this.getJspBody();
		if(jf!=null) {
			//将标签提内容拷贝到sw中
			jf.invoke(sw);
			content=sw.toString();
			//使用Md2x解析内容
			content=md2x.parse(content);
			
		}else if(value!=null) {
			//解析属性value中的markdwon文本
			content=md2x.parse(value);
			
		}
		//获取JspWriter将内容输出到jsp页面
		JspWriter out=getJspContext().getOut();
		out.println(content);
	}
	
}
