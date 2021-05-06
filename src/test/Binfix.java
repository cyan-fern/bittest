package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Binfix {
	static int[] ca=new int[] {'0','1'};
	static int mask=(1<<7);
	private long floc;
	private int erem;
	private int write;
	private int read;
	private int writenum;
	private int readnum;
	
	public Binfix() {
		this.writenum=0;
		this.readnum=0;
	}
	
	public void fix(FileInputStream in,FileOutputStream out) throws IOException {
		this.initreadcopyproto(in,out);
		this.prewrite(out);
		int ch=in.read();
		while(ch!=13) {
			this.writec(out,ch);
			ch=in.read();
		}
		this.writeflush(out);
	}
	public void revert(FileInputStream in,FileOutputStream out) throws IOException {
		this.finalreadcopyproto(in, out);
		int[] buffer = this.loadread(in);
		for(int i=0;i<buffer.length;i++) {
			out.write(ca[buffer[i]]);
		}
		out.write(13);
		out.write(10);
	}
	
	private void prewrite(FileOutputStream file) throws IOException {
		this.floc=file.getChannel().position();
		file.write(0);
	}
	private void write(FileOutputStream file,int num) throws IOException {
		if(writenum==8) {this.writenum=0;file.write(write);this.write=0;}
		this.write=write<<1|num;writenum++;
	}
	public void writeflush(FileOutputStream file) throws IOException {
		int remaining;
		if(writenum==0) {remaining=0;}
		else {
			remaining=8-writenum;
			file.write(write<<remaining);}
		file.getChannel().position(floc);
		file.write(remaining);
	}
	public void initreadproto(FileInputStream file) throws IOException {
		int bn=1;
		int b;
		while(bn!=0) {
			b=file.read();
			if(b=='^') {bn++;}
			else if(b==13) {file.read();bn--;}//stupid nonsense windows newline garbage
			else {bn--;}
		}
		file.read();file.read();//discard (stupid nonsense windows) newline (garbage)
	}
	public void initreadcopyproto(FileInputStream in,FileOutputStream out) throws IOException {
		int bn=2;
		int b;
		while(bn!=0) {
			b=in.read();
			if(b=='^') {bn++;}
			else {bn--;}
			out.write(b);//copy
		}
		in.read();out.write(in.read());//copy
	}
	public void finalreadcopyproto(FileInputStream in,FileOutputStream out) throws IOException {
		int bn=2;
		int b;
		while(bn!=0) {
			b=in.read();
			if(b=='^') {bn++;}
			else {bn--;}
			out.write(b);//copy
		}
		out.write(13);out.write(in.read());//copy
	}
	public void preread(FileInputStream file) throws IOException {
		erem=file.read();
	}
	public int iread(FileInputStream file) throws IOException {
		if(readnum==0) {this.readnum=8;read=file.read();}
		readnum--;int ret=read&mask;
		read<<=1;
		return ret>>7;
	}
	public int[] loadread(FileInputStream file) throws IOException {
		this.preread(file);
		int[]ra=new int[(file.available()-(erem<0?1:0))*8+erem];
		for(int i=0;i<ra.length;i++) {
			ra[i]=iread(file);
		}
		return ra;
	}
	
	

	public void write(FileOutputStream file,Boolean num) throws IOException {
		if(num) {write(file,1);}
		else {write(file,0);}
	}
	public void writec(FileOutputStream file,int ch) throws IOException {
		if(ch=='1') {write(file,1);}
		else {write(file,0);}
	}
}
