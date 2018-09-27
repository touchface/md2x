package top.touchface.md2x;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import top.touchface.md2x.entity.Options;
import top.touchface.md2x.entity.Tokens;
import top.touchface.md2x.lexer.BlockLexer;


/**
 * Md2x a markdown parser.
 * 
 * @author touchface
 * @date 2018-09-26 20:29
 */
public class Md2x {
	
	private static String encoding = "UTF-8";
	
	private Options options;//解析器设置参数
	private BlockLexer lexer;//块级元素分词器
	private Parser parser;//解析器对象
	
	
	public Md2x() {
		
		this.options=new Options();
		this.lexer=new BlockLexer(options);
		this.parser=new Parser(options);
	}
	
	/**
	 * The constructor receives the user to set parameters for the parser.
	 * 
	 * @param options
	 */
	public Md2x(Options options) {
		this.options=options;
		this.lexer=new BlockLexer(this.options);
		this.parser=new Parser(options);
	}
	
	/**
	 * Parsing markdown tags into HTML tags
	 * 
	 * @param src markdown text
	 * @return html
	 */
	public String parse(String src) {
		
		//对文档进行分词处理
		Tokens tokens=lexer.lex(src);
	
		return parser.parse(tokens);
	}
	
	/**
	 * Get the markdown text from the file, which is parsed as HTML.
	 * 
	 * @param file markdown file
	 * @return html
	 */
	public String parse(File file) {
		
		return this.parse(readFile(file));
		
	}
	
	/**
	 * Read the markdown text in the Markdown file.
	 *
	 * @param file
	 * @return markdown文本
	 */
	private String readFile(File file) {
		
		if (file.isFile() && file.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String line = null;
				StringBuffer sb = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line + "\n");
				}

				bufferedReader.close();
				return sb.toString();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		} else {
			System.out.println("md2x:file not found!");
		}
		return null;
	}
	
}
