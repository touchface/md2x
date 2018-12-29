package top.touchface.md2x.tag;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import top.touchface.md2x.Md2x;
import top.touchface.md2x.entity.Options;
/**
 * JSP自定义标签，使用Md2x的默认配置来解析Markdown
 *
 * @author touchface
 * date 2018-09-28 13:39
 */
public class Md2xTag extends SimpleTagSupport {

	private Md2x md2x;// Md2x对象
    private String value;// 需要解析的文本
    private Options options;// 设置对象
    private boolean isChanged;// 用于判断设置参数是否发生变

	public Md2xTag(){
	    options=new Options();
	    md2x=new Md2x(options);
    }

	@Override
	public void doTag() throws IOException {
        System.out.println("value:"+value);
        System.out.println("baseUrl:"+options.baseUrl);
	    // 判断设置信息是否发生改变，发生改变，则重新创建MD2x
        if(isChanged){
            this.remakeMd2x();
        }
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
                content = md2x.parse(value);

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

    /**
     * 根据新的设置信息，重新创建Md2x实例
     */
	private void remakeMd2x(){
	    md2x=new Md2x(options);
	    isChanged=false;
    }

	public void setValue(String value) {
		this.value = value;

	}
	public void setGfm(boolean gfm){
	    if(this.options.gfm!=gfm){
	        isChanged=true;
            this.options.gfm=gfm;
        }
    }
    public void setBreaks(boolean breaks){
        if(this.options.breaks!=breaks){
            isChanged=true;
            this.options.breaks=breaks;
        }

    }
    public void setTables(boolean tables){
        if(this.options.tables!=tables){
            isChanged=true;
            this.options.tables=tables;
        }

    }
    public void setPedantic(boolean pedantic){
        if(this.options.pedantic!=pedantic){
            isChanged=true;
            this.options.pedantic=pedantic;
        }

    }
    public void setHeaderIds(boolean headerIds){
        if(this.options.headerIds!=headerIds){
            isChanged=true;
            this.options.headerIds=headerIds;
        }

    }
    public void setHeaderPrefix(String headerPrefix){
        if(headerPrefix!=null&&!headerPrefix.equals(this.options.headerPrefix)){
            isChanged=true;
            this.options.headerPrefix=headerPrefix;
        }

    }
    public void setBaseUrl(String baseUrl){
        if(baseUrl!=null&&!baseUrl.equals(this.options.baseUrl)){
            isChanged=true;
            this.options.baseUrl=baseUrl;
        }

    }
    public void setMangle(boolean mangle){
        if(this.options.mangle!=mangle){
            isChanged=true;
            this.options.mangle=mangle;
        }

    }
    public void setSanitize(boolean sanitize){
        if(this.options.sanitize!=sanitize){
            isChanged=true;
            this.options.sanitize=sanitize;
        }

    }
    public void setSmartLists(boolean smartLists){
        if(this.options.smartLists!=smartLists){
            isChanged=true;
            this.options.smartLists=smartLists;
        }

    }
    public void setSmartypants(boolean smartypants){
        if(this.options.smartypants!=smartypants){
            isChanged=true;
            this.options.smartLists=smartypants;
        }

    }
    public void setXhtml(boolean xhtml){
        if(this.options.xhtml!=xhtml){
            isChanged=true;
            this.options.xhtml=xhtml;
        }
    }

}
