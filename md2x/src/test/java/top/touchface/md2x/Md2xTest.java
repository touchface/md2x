package top.touchface.md2x;

import org.junit.Test;

import top.touchface.md2x.entity.Options;

public class Md2xTest {
	
	@Test
	public void test1(){
		Options options=new Options();
		options.baseUrl="http://localhost:8080/files/";
		options.headerPrefix="ABC";
		System.out.println(Md2x.parseToHtml("## 哈哈",options));
	}
	
}
