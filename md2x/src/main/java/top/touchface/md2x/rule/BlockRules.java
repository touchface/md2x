package top.touchface.md2x.rule;

import java.util.regex.Pattern;

/**
 * Block grammar
 * 
 * @author touchface
 * @date 2018-09-26 21:01
 */
public class BlockRules{
	
	//Normal Block Grammar
	public Rule newline=null;
	public Rule heading=null;
	public Rule lheading=null;

	public Rule code=null;
	public Rule fences=null;
	public Rule nptable=null;
	public Rule table=null;
	public Rule hr=null;
	public Rule blockquote=null;
	public Rule paragraph=null;
	public Rule text=null;
	public Rule list=null;
	public Rule html=null;
	
	public Rule def=null;
	public Rule bullet=null;
	public Rule item=null;
	
	
	public BlockRules() {
		this.setNormal();
	}

	/**
	 * Normal Grammar
	 */
	private void setNormal() {
		
		this.newline=new Rule(RegexLib.newline);
		this.heading=new Rule(RegexLib.heading);
		this.lheading=new Rule(RegexLib.lheading);
		this.code=new Rule(RegexLib.code);
		this.hr=new Rule(RegexLib.hr);
		this.blockquote=new Rule(RegexLib.blockquote);
		this.paragraph=new Rule(RegexLib.paragraph);
		this.text=new Rule(RegexLib.text);
		this.list=new Rule(RegexLib.list);
		this.html=new Rule(RegexLib.html);
		this.def=new Rule(RegexLib.def);
		this.bullet=new Rule(RegexLib.bullet);
		this.item=new Rule(RegexLib.item);
		
		this.paragraph.replace("hr", RegexLib.hr)
				 .replace("heading",RegexLib.heading)
				 .replace("lheading",RegexLib.lheading)
				 .replace("tag", RegexLib._tag);
		
		this.blockquote.replace("paragraph",this.paragraph.getRegex());
		
		this.html.setFlag(Pattern.CASE_INSENSITIVE);
		this.html.replace("comment",RegexLib._comment)
			.replace("tag",RegexLib._tag)
			.replace("attribute",RegexLib._attribute);
		
		this.def.replace("label",RegexLib._label)
		    	.replace("title",RegexLib._title);
		
		item.setFlag(Pattern.MULTILINE);
		item.replace("bull/g",RegexLib.bullet);
		
		list.replace("bull/g",RegexLib.bullet)
			.replace("hr","\\n+(?=\\1?(?:(?:- *){3,}|(?:_ *){3,}|(?:\\* *){3,})(?:\\n+|$))")
			.replace("def", "\\n+(?=" +RegexLib.def+ ")");
		
	}
	
	/**
	 * GFM Grammar
	 */
	public void setGfm() {
		String replacement="(?!"+RegexLib._gfm_fences.replaceAll("\\\\1","\\\\2")+"|"
		+list.getRegex().replaceAll("\\\\1", "\\\\3")+"|";
		this.paragraph.replace("(?!", replacement);
		this.heading=new Rule(RegexLib._gfm_heading);
		this.fences=new Rule(RegexLib._gfm_fences);
		

	}
	
	/**
	 * GFM Table Grammar 
	 */
	public void setGfmTables() {
		
		this.nptable=new Rule(RegexLib._gfm_nptable);
		this.table=new Rule(RegexLib._gfm_table);
	}
	
	/**
	 * Pedantic Grammar
	 */
	public void setPedantic() {
		
		this.html=new Rule(RegexLib._pedantic_html).replace("comment", RegexLib._comment)
		  .replace("tag/g","(?!(?:"
			      + "a|em|strong|small|s|cite|q|dfn|abbr|data|time|code|var|samp|kbd|sub"
			      + "|sup|i|b|u|mark|ruby|rt|rp|bdi|bdo|span|br|wbr|ins|del|img)"
			      + "\\b)\\w+(?!:|[^\\w\\s@]*@)\\b");
		
		this.def=new Rule(RegexLib._pedantic_def);
	}
	
}
