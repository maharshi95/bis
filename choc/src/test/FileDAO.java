package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class FileDAO {
	
	public int getFileCount() {
		int count = 0;
		FileReader reader = null;
		BufferedReader br = null;
		PrintWriter writer = null;
		
		try {
			File file = new File("FileCounter.initial");
			
			if(!file.exists()) {
				file.createNewFile();
				writer = new PrintWriter(new FileWriter(file));
				writer.println(0);
				
			}
			if(writer != null) {
				writer.close();
			}
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			String initial = br.readLine();
			count = Integer.parseInt(initial);
		} catch (Exception e) {
			if(writer != null)
				writer.close();
		}
		if(br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public void save(int count) throws Exception {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		fileWriter = new FileWriter("FileCounter.initial");
		printWriter = new PrintWriter(fileWriter);
		printWriter.println(count);
		
		if(printWriter != null) {
			printWriter.close();
		}
	}
}
