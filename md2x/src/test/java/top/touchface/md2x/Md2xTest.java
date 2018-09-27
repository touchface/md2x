package top.touchface.md2x;

import java.io.File;

import org.junit.Test;

import top.touchface.md2x.entity.Options;

public class Md2xTest {
	
	@Test
	public void test1(){
		Options options=new Options();
		options.sanitize=false;
		Md2x md2x=new Md2x(options);

		System.out.println(md2x.parse(new File("F://test1.md")));
	}
	
}
