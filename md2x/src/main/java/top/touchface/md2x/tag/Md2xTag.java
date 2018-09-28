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
 * Using custom tags in Jsp pages to implement Markdown parsing
 * 
 * @author touchface
 * @date 2018-09-28 13:39
 */
public class Md2xTag extends SimpleTagSupport{
	
	private String value;//接收markdown文本
	private boolean gfm=true;//是否支持gfm语法
	private boolean pedantic=false;//是否支持pedantic语法
	private boolean headerIds=true;//是否开启分级标题id自动生成
	private String headerPrefix="";//设置id前缀
	private String baseUrl=null;//设置连接的基本路径
	
	public void setValue(String value) {
		this.value=value;
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
	private Options getOptions(){
		Options options=new Options();
		options.gfm=this.gfm;
		options.pedantic=this.pedantic;
		options.headerIds=this.headerIds;
		options.headerPrefix=this.headerPrefix;
		options.baseUrl=this.baseUrl;
		return options;
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
			content=Md2x.parseToHtml(content,this.getOptions());
			
		}else if(value!=null) {
			//解析属性value中的markdwon文本
			content=Md2x.parseToHtml(value,this.getOptions());
			
		}
		//获取JspWriter将内容输出到jsp页面
		JspWriter out=getJspContext().getOut();
		out.println(content);
	}
	
}
