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
 * Md2x一款Markdown解析器
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
	 * 构造函数，接受设置参数对分词器和解析器进行设置
	 * 
	 * @param options 设置
	 */
	public Md2x(Options options) {
		this.options=options;
		this.lexer=new BlockLexer(this.options);
		this.parser=new Parser(options);
	}
	/**
	 * 静态方法，用于MARKDOWN转换为HTML。
	 * 
	 * @param src MARKDOWN文本
	 * @param options 设置
	 * @return result HTML标记
	 */
	public static String parseToHtml(String src,Options options){
		Md2x md2x=new Md2x(options);
		return md2x.parse(src);
	}
	/**
	 * 静态方法，用于MARKDOWN转换为HTM，使用默认的设置进行解析。
	 * 
	 * @param src MARKDOWN文本
	 * @return result HTML标记
	 */
	public static String parseToHtml(String src){
		Md2x md2x=new Md2x();
		return md2x.parse(src);
	}
	
	/**
	 * 静态方法，读取MARKDOWN文档中的标记，解析为HTML。
	 * 
	 * @param file MARKDOWN文件
	 * @param options 设置
	 * @return result HTML标记
	 */
	public static String parseToHtml(File file,Options options){
		Md2x md2x=new Md2x(options);
		return md2x.parse(file);
	}
	
	/**
	 * 静态方法，读取MARKDOWN文档中的标记，解析为HTML,使用默认设置进行解析。
	 * 
	 * @param file  MARKDOWN文件
	 * @return result HTML标记
	 */
	public static String parseToHtml(File file){
		Md2x md2x=new Md2x();
		return md2x.parse(file);
	}
	
	/**
	 * 将MARKDOWN标记解析为HTML标记
	 * 
	 * @param src MARKDOWN标记
	 * @return HTML标记
	 */
	public String parse(String src) {
		
		//对文档进行分词处理
		Tokens tokens=lexer.lex(src);
	
		return parser.parse(tokens);
	}
	
	/**
	 * 读取MARKDOWN文档中的标记，解析为HTML标记
	 * 
	 * @param file MARKDOWN文件
	 * @return HTML标记
	 */
	public String parse(File file) {
		
		return this.parse(readFile(file));
		
	}
	/**
	 * 读取MARKDOWN文件中的文本
	 * 
	 * @param file MARKDOWN文件
	 * @return MARKDOWN文本
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
