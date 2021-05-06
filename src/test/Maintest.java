package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Maintest {

	public static void main(String[] args) throws Exception {
		Files.deleteIfExists(Paths.get("fixmessage.txt"));
		Files.deleteIfExists(Paths.get("revertmessage.txt"));
		fix("encmessage.txt","fixmessage.txt");
		revert("fixmessage.txt","revertmessage.txt");
	}
	
	public static void fix(String sin,String sout) throws Exception {
		FileInputStream in = null;
		FileOutputStream out = null;
		in = new FileInputStream(sin);
		out = new FileOutputStream(sout);
		Binfix bin = new Binfix();
		
		bin.fix(in,out);
		
		in.close();
		out.close();
	}
	
	public static void revert(String sin,String sout) throws Exception {
		FileInputStream in = null;
		FileOutputStream out = null;
		in = new FileInputStream(sin);
		out = new FileOutputStream(sout);
		Binfix bin = new Binfix();
		
		bin.revert(in,out);
		
		in.close();
		out.close();
	}
	
	
	public static void testprint(FileInputStream in) throws IOException {
		for(int i=0;i<5;i++) {
			System.out.print(String.format("%c",in.read()));
		}
	}

}
