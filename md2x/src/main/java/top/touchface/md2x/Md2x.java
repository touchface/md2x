package top.touchface.md2x;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.entity.Tokens;
import top.touchface.md2x.lexer.BlockLexer;

/**
 * Md2x一款Markdown解析器
 * 
 * @author touchface
 * date 2018-09-26 20:29
 */
public class Md2x {

	private BlockLexer lexer;//块级元素分词器
	private Parser parser;//解析器对象
	public Md2x() {
		Options options=new Options();
		this.lexer=new BlockLexer(options);
		this.parser=new Parser(options);
	}
	
	/**
	 * 构造函数，接受设置参数对分词器和解析器进行设置
	 * 
	 * @param options 设置
	 */
	public Md2x(Options options) {
		this.lexer=new BlockLexer(options);
		this.parser=new Parser(options);
	}

	/**
	 * 将MARKDOWN标记解析为HTML标记
	 * 
	 * @param src MARKDOWN标记
	 * @return HTML标记
	 */
	public String parse(String src) {
		// 对文档进行分词处理
		Tokens tokens=lexer.lex(src);
		// 解析Token
		return parser.parse(tokens);
	}
	
}
