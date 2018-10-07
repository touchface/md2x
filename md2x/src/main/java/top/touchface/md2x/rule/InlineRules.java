package top.touchface.md2x.rule;


/**
 * 行内元素的语法
 * 
 * @author touchface
 * @date 2018-09-26 21:04
 */
public class InlineRules {
	//Inline Block Grammar
	public Rule escape=null;
	public Rule autolink=null;
	public Rule url=null;
	public Rule tag=null;
	public Rule link=null;
	public Rule reflink=null;
	public Rule nolink=null;
	public Rule strong=null;
	public Rule em=null;
	public Rule code=null;
	public Rule br=null;
	public Rule del=null;
	public Rule text=null;
	public Rule _gfm_backpedal=null;
	
	public InlineRules() {
		this.setNormal();
	}
	public void setNormal() {
		
		this.escape=new Rule(RegexLib.i_escape);
		this.autolink=new Rule(RegexLib.i_autolink);
		this.tag=new Rule(RegexLib.i_tag);
		this.link=new Rule(RegexLib.i_link);
		this.reflink=new Rule(RegexLib.i_reflink);
		this.nolink=new Rule(RegexLib.i_nolink);
		this.strong=new Rule(RegexLib.i_strong);
		this.em=new Rule(RegexLib.i_em);
		this.code=new Rule(RegexLib.i_code);
		this.br=new Rule(RegexLib.i_br);
		this.text=new Rule(RegexLib.i_text);
		
		this.autolink.replace("scheme", RegexLib._i_sheme)
		    		 .replace("email",RegexLib._i_email);
		
		this.tag.replace("comment",RegexLib._comment)
		   		.replace("attribute",RegexLib._i_attribute);
		
		this.link.replace("label",RegexLib._i_label)
				 .replace("href", RegexLib._i_href)
				 .replace("title", RegexLib._i_title);
		
		this.reflink.replace("label",RegexLib._i_label);
		
		
		
	}
	
	public void setPedantic() {
		
		this.link=new Rule(RegexLib._i_pedantic_link).replace("label",RegexLib._i_label);
		this.reflink=new Rule(RegexLib._i_pedantic_reflink).replace("label",RegexLib._i_label);
		this.strong=new Rule(RegexLib._i_pedantic_strong);
		this.em=new Rule(RegexLib._i_pedantic_em);
		
		
	}
	
	public void setGfm() {
		
		this.escape.replace("])","~|])");
		
		this.url=new Rule(RegexLib._i_gfm_url);
		this.url.replace("email", RegexLib._i_email);
		
		this.del=new Rule(RegexLib._i_gfm_del);
		
		this._gfm_backpedal=new Rule(RegexLib._i_gfm_backpedal);
		
		this.text.replace("]|", "~]|")
				 .replace("|", "|https?://|ftp://|www\\\\.|[a-zA-Z0-9.!#$%&\\'*+/=?^_`{\\\\|}~-]+@|");
		
	}
	public void setGfmBreaks() {
		
		this.setGfm();
		this.br.replace("{2,}","*");
		this.text.replace("{2,}", "*");
		
	}
	
}
