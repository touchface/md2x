package top.touchface.md2x;

import java.io.File;

import org.junit.Test;

import top.touchface.md2x.entity.Options;

public class Md2xTest {
	
	@Test
	public void test1(){
		Options options=new Options();
		options.headerPrefix="md2x";
		System.out.println(Md2x.parseToHtml(new File("F:/md2x.md"),options));
	}
	
}
