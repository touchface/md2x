package top.touchface.md2x.expand;
/**
 * 过滤器，用于过滤MARKDOWN文本中可能纯在的恶意代码
 * （暂未实现，可以自行继承本类中的方法，并在Options中进行设置启用）
 * @author touchface
 * date 2018-10-07 21:12
 */
public abstract class Sanitizer {
	public abstract String sanitize(String text);
}
