package top.touchface.md2x;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import top.touchface.md2x.entity.Link;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.entity.Token;
import top.touchface.md2x.entity.Tokens;
import top.touchface.md2x.entity.Type;
import top.touchface.md2x.lexer.InlineLexer;
import top.touchface.md2x.render.HtmlRenderer;
import top.touchface.md2x.render.Renderer;
import top.touchface.md2x.render.TextRender;
import top.touchface.md2x.utils.Helper;

/**
 * 解析MARKDOWN行内标记，并编译为HTML
 * 
 * @author touchface
 * @date 2018-09-26 20:28
 */
public class Parser {

	private LinkedList<Token> tokens;// BlockLexer得到的块级元素的标记
	private Map<String, Link> links;// BlockLexer得到的链接

	private Token token;// 当前解析的Token
	private Options options;// 对解析器的设置
	private Renderer renderer;// 渲染器对象，对Token进行渲染

	private InlineLexer inlineLexer;// 行内元素解析器
	private InlineLexer textLexer;// 行内元素解析器
	/**
	 * 对解析器进行设置
	 * 
	 * @param options 设置
	 */
	public Parser(Options options) {
		if (options != null) {
			this.options = options;
			if (this.options.renderer == null) {
				this.options.renderer = new HtmlRenderer(this.options);
			}
		} else {
			
			this.options = new Options();
			this.options.renderer = new HtmlRenderer(this.options);
		}
		this.renderer = this.options.renderer;

	}
	
	/**
	 * 解析循环
	 * 
	 * @param tokens MARKDOWN标记的块级元素和连接
	 * 
	 * @return HTML
	 */
	public String parse(Tokens tokens) {
		
		this.links = tokens.links;
		this.tokens = tokens.tokens;
		this.inlineLexer = new InlineLexer(tokens.links, options);
		
		Options opt = this.options.clone();
		opt.renderer = new TextRender(options);
		this.textLexer = new InlineLexer(links, opt);
		String out = "";
		while (this.next() != null) {
			out += this.parseToken();
		}
		return out;
	}

	/**
	 * 解析当前的标记
	 * 
	 * @return 解析结果
	 */
	private String parseToken() {

		switch (this.token.type) {

		case space:
			return "";

		case hr:
			return this.renderer.hr();

		case heading:
			
			return this.renderer.heading(this.inlineLexer.output(this.token.text), this.token.depth,
					Helper.unescape(this.textLexer.output(this.token.text)));

		case code: {
			return this.renderer.code(this.token.text, this.token.lang, this.token.escaped);
		}

		case table: {
			
			String header = "", body = "";
			// header
			String cell = "";
			for (int i = 0; i < this.token.header.size(); i++) {
				cell += this.renderer.tablecell(this.inlineLexer.output(this.token.header.get(i)), true,
						token.align.get(i));
			}
			header += this.renderer.tablerow(cell);

			for (int i = 0; i < this.token.cells.size(); i++) {

				List<String> row = this.token.cells.get(i);
				cell = "";
				for (int j = 0; j < row.size(); j++) {
					cell += this.renderer.tablecell(this.inlineLexer.output(row.get(j)), false,
							this.token.align.get(j));
				}
				body += this.renderer.tablerow(cell);
			}
			return this.renderer.table(header, body);
		}

		case blockquote_start: {
			String body = "";

			while (this.next().type != Type.blockquote_end) {
				body += this.parseToken();
			}
			return this.renderer.blockquote(body);
		}
		case list_start: {
			String body = "";
			boolean ordered = this.token.ordered;
			String start = this.token.start;
			
			while (this.next().type != Type.list_end) {
				body += this.parseToken();
			}
			return this.renderer.list(body, ordered, start);
		}
		case list_item_start: {
			String body = "";

			if (this.token.task) {
				body += this.renderer.checkbox(this.token.checked);
			}

			while (this.next().type != Type.list_item_end) {
				body += this.token.type == Type.text ? this.parseText() : this.parseToken();
			}
			return this.renderer.listitem(body);
		}

		case loose_item_start: {
			String body = "";

			while (this.next().type != Type.list_item_end) {
				body += this.parseToken();
			}
			return this.renderer.listitem(body);
		}

		case html: {
			
			return this.renderer.html(this.token.text);
		}

		case paragraph: {
			return this.renderer.paragraph(this.inlineLexer.output(this.token.text));
		}

		case text: {
			return this.renderer.paragraph(this.parseText());
		}
		default:
			break;
		}
		return null;
	}

	/**
	 * 解析文本标记
	 *  
	 * @return
	 */
	private String parseText() {
		
		String body = this.token.text;
		while (this.peek().type == Type.text) {
			body += "\n" + this.next().text;
		}

		return this.inlineLexer.output(body);
	}

	/**
	 * 切换到下一个标记
	 * 
	 * @return
	 */
	private Token next() {
		if(this.tokens.size()>0) {
			return this.token = this.tokens.removeFirst();
		}else {
			return null;
		}
	}

	/**
	 * 预览下一个标记
	 * 
	 * @return
	 */
	private Token peek() {
		if(tokens.size()==0) {
			return null;
		}
		if (tokens.size() > 1) {
			return this.tokens.get(1);
		} else{
			return this.tokens.get(0);
		}
	}

}
