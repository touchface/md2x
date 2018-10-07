/**
 * 本解析器的设计和实现参考了marked.js的源码，项目中使用它的的正则表达式对MARKDOWN标记进行匹配。
 * 这是一个非常好的markdown解析器开源项目
 * 以下为marked项目的LISENCE: 
 *-----------------------------------------------------------------------
 *
 * marked - a markdown parser
 * Copyright (c) 2011-2018, Christopher Jeffrey. (MIT Licensed)
 * https://github.com/markedjs/marked
 * 
 * ----------------------------------------------------------------------
 */
package top.touchface.md2x.rule;
/**
 * 对MARKDOWN标记进行匹配的正则表达式库，包含了对各种标记进行匹配的正则表达式
 */
public class RegexLib {
	
	//Normal block grammar
	public static final String newline ="^\\n+";
	public static final String heading ="^ *(#{1,6}) *([^\\n]+?) *(?:#+ *)?(?:\\n+|$)";
	public static final String lheading ="^([^\\n]+)\\n *(=|-){2,} *(?:\\n+|$)";
	public static final String code ="^( {4}[^\\n]+\\n*)+";
	public static final String hr ="^ {0,3}((?:- *){3,}|(?:_ *){3,}|(?:\\* *){3,})(?:\\n+|$)";
	public static final String blockquote ="^( {0,3}> ?(paragraph|[^\\n]*)(?:\\n|$))+";
	public static final String paragraph ="^([^\\n]+(?:\\n(?!hr|heading|lheading| {0,3}>|<\\/?(?:tag)(?: +|\\n|\\/?>)|<(?:script|pre|style|!--))[^\\n]+)*)";
	public static final String text ="^[^\\n]+";
	public static final String list ="^( *)(bull) [\\s\\S]+?(?:hr|def|\\n{2,}(?! )(?!\\1bull )\\n*|\\s*$)";
	public static final String html ="^ {0,3}(?:" // optional indentation
		    + "<(script|pre|style)[\\s>][\\s\\S]*?(?:</\\1>[^\\n]*\\n+|$)" // (1)
		    + "|comment[^\\n]*(\\n+|$)" // (2)
		    + "|<\\?[\\s\\S]*?\\?>\\n*" // (3)
		    + "|<![A-Z][\\s\\S]*?>\\n*" // (4)
		    + "|<!\\[CDATA\\[[\\s\\S]*?\\]\\]>\\n*" // (5)
		    + "|</?(tag)(?: +|\\n|/?>)[\\s\\S]*?(?:\\n{2,}|$)" // (6)
		    + "|<(?!script|pre|style)([a-z][\\w-]*)(?:attribute)*? */?>(?=\\h*\\n)[\\s\\S]*?(?:\\n{2,}|$)" // (7) open tag
		    + "|</(?!script|pre|style)[a-z][\\w-]*\\s*>(?=\\h*\\n)[\\s\\S]*?(?:\\n{2,}|$)" // (7) closing tag
		    + ")";
	public static final String def ="^ {0,3}\\[(label)\\]: *\\n? *<?([^\\s>]+)>?(?:(?: +\\n? *| *\\n *)(title))? *(?:\\n+|$)";
	public static final String bullet ="(?:[*+-]|\\d+\\.)";
	public static final String item ="^( *)(bull) [^\\n]*(?:\\n(?!\\1bull )[^\\n]*)*";
	public static final String _tag ="address|article|aside|base|basefont|blockquote|body|caption"
			+ "|center|col|colgroup|dd|details|dialog|dir|div|dl|dt|fieldset|figcaption"
			+ "|figure|footer|form|frame|frameset|h[1-6]|head|header|hr|html|iframe"
			+ "|legend|li|link|main|menu|menuitem|meta|nav|noframes|ol|optgroup|option"
			+ "|p|param|section|source|summary|table|tbody|td|tfoot|th|thead|title|tr"
			+ "|track|ul";
	
	public static final String _comment ="<!--(?!-?>)[\\s\\S]*?-->";
	public static final String _attribute =" +[a-zA-Z:_][\\w.:-]*(?: *= *\"[^\"\\n]*\"| *= *'[^'\\n]*'| *= *[^\\s\"'=<>`]+)?";
	public static final String _label ="(?!\\s*\\])(?:\\\\[\\[\\]]|[^\\[\\]])+";
	public static final String _title ="(?:\"(?:\\\\\"?|[^\"\\\\])*\"|'[^'\\n]*(?:\\n[^'\\n]+)*\\n?'|\\([^()]*\\))";
			
	//GFM Block Grammar
	public static final String _gfm_fences ="^ *(`{3,}|~{3,})[ \\.]*(\\S+)? *\\n([\\s\\S]*?)\\n? *\\1 *(?:\\n+|$)";
	public static final String _gfm_heading ="^ *(#{1,6}) +([^\\n]+?) *#* *(?:\\n+|$)";
	public static final String _gfm_nptable ="^ *([^|\\n ].*\\|.*)\\n *([-:]+ *\\|[-| :]*)(?:\\n((?:.*[^>\\n ].*(?:\\n|$))*)\\n*|$)";
	public static final String _gfm_table ="^ *\\|(.+)\\n *\\|?( *[-:]+[-| :]*)(?:\\n((?: *[^>\\n ].*(?:\\n|$))*)\\n*|$)";
	
	//Pedantic Block Grammar
	public static final String _pedantic_html="^ *(?:comment *(?:\\n|\\s*$)"
		    + "|<(tag)[\\s\\S]+?</\\1> *(?:\\n{2,}|\\s*$)" // closed tag
		    + "|<tag(?:\"[^\"]*\"|\'[^\']*\'|\\s[^\'\"/>\\s]*)*?/?> *(?:\\n{2,}|\\s*$))";
	
	public static final String _pedantic_def="^ *\\[([^\\]]+)\\]: *<?([^\\s>]+)>?(?: +([\"(][^\\n]+[\")]))? *(?:\\n+|$)";
	
	//Inline Block Grammar
	public static final String i_escape="^\\\\([!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~])";
	public static final String i_autolink="^<(scheme:[^\\s\\x00-\\x1f<>]*|email)>";
	public static final String i_tag="^comment"
		    + "|^</[a-zA-Z][\\w:-]*\\s*>" // self-closing tag
		    + "|^<[a-zA-Z][\\w-]*(?:attribute)*?\\s*/?>" // open tag
		    + "|^<\\?[\\s\\S]*?\\?>" // processing instruction, e.g. <?php ?>
		    + "|^<![a-zA-Z]+\\s[\\s\\S]*?>" // declaration, e.g. <!DOCTYPE html>
		    + "|^<!\\[CDATA\\[[\\s\\S]*?\\]\\]>";
	public static final String i_link="^!?\\[(label)\\]\\(href(?:\\s+(title))?\\s*\\)";
	public static final String i_reflink="^!?\\[(label)\\]\\[(?!\\s*\\])((?:\\\\[\\[\\]]?|[^\\[\\]\\\\])+)\\]";
	public static final String i_nolink="^!?\\[(?!\\s*\\])((?:\\[[^\\[\\]]*\\]|\\\\[\\[\\]]|[^\\[\\]])*)\\](?:\\[\\])?";
	public static final String i_strong="^__([^\\s][\\s\\S]*?[^\\s])__(?!_)|^\\*\\*([^\\s][\\s\\S]*?[^\\s])\\*\\*(?!\\*)|^__([^\\s])__(?!_)|^\\*\\*([^\\s])\\*\\*(?!\\*)";
	public static final String i_em="^_([^\\s][\\s\\S]*?[^\\s_])_(?!_)|^_([^\\s_][\\s\\S]*?[^\\s])_(?!_)|^\\*([^\\s][\\s\\S]*?[^\\s*])\\*(?!\\*)|^\\*([^\\s*][\\s\\S]*?[^\\s])\\*(?!\\*)|^_([^\\s_])_(?!_)|^\\*([^\\s*])\\*(?!\\*)";
	public static final String i_code="^(`+)\\s*([\\s\\S]*?[^`]?)\\s*\\1(?!`)";
	public static final String i_br="^ {2,}\\n(?!\\s*$)";
	public static final String i_text="^[\\s\\S]+?(?=[\\\\<!\\[`*]|\\b_| {2,}\\n|$)";
	public static final String _i_escape="\\\\([!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~])";
	public static final String _i_email="[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+(@)[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+(?![-_])";
	public static final String _i_attribute="\\s+[a-zA-Z:_][\\w.:-]*(?:\\s*=\\s*\"[^\"]*\"|\\s*=\\s*'[^']*'|\\s*=\\s*[^\\s\"'=<>`]+)?";
	public static final String _i_sheme="[a-zA-Z][a-zA-Z0-9+.-]{1,31}";
	public static final String _i_label="(?:\\[[^\\[\\]]*\\]|\\\\[\\[\\]]?|`[^`]*`|[^\\[\\]\\\\])*?";
	public static final String _i_href="\\s*(<(?:\\\\[<>]?|[^\\s<>\\\\])*>|(?:\\\\[()]?|\\([^\\s\\x00-\\x1f()\\\\]*\\)|[^\\s\\x00-\\x1f()\\\\])*?)";
	public static final String _i_title="\"(?:\\\\\"?|[^\"\\\\])*\"|'(?:\\\\'?|[^'\\\\])*'|\\((?:\\\\\\)?|[^)\\\\])*\\)";
	
	public static final String _i_pedantic_strong="^__(?=\\S)([\\s\\S]*?\\S)__(?!_)|^\\*\\*(?=\\S)([\\s\\S]*?\\S)\\*\\*(?!\\*)";
	public static final String _i_pedantic_em="^_(?=\\S)([\\s\\S]*?\\S)_(?!_)|^\\*(?=\\S)([\\s\\S]*?\\S)\\*(?!\\*)";
	public static final String _i_pedantic_link="^!?\\[(label)\\]\\((.*?)\\)";
	public static final String _i_pedantic_reflink="/^!?\\[(label)\\]\\s*\\[([^\\]]*)\\]/";
	
	public static final String _i_gfm_url="^((?:ftp|https?):\\/\\/|www\\.)(?:[a-zA-Z0-9\\-]+\\.?)+[^\\s<]*|^email";
	public static final String _i_gfm_backpedal="(?:[^?!.,:;*_~()&]+|\\([^)]*\\)|&(?![a-zA-Z0-9]+;$)|[?!.,:;*_~)]+(?!$))+";
	public static final String _i_gfm_del="^~~(?=\\S)([\\s\\S]*?\\S)~~";
	
}
