package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MFileIO {
	public static final String ORIGINAL_PATH = "D:\\OOP\\data";

	private boolean WriteToFile(String content, String filePath, String fileName) {
		try {
			File directory = new File(filePath);
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(filePath + "\\" + fileName);
			if (!file.exists())
				file.createNewFile();

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private BufferedReader ReadFromFile(String filePath, String fileName) {
		BufferedReader reader = null;
		try {
			File directory = new File(filePath);
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(filePath + "\\" + fileName);
			if (!file.exists())
				file.createNewFile();
			reader = new BufferedReader(new FileReader(filePath + "\\" + fileName));
		} catch (Exception e) {
			System.out.println("BufferedReader:" + fileName + "fail");
		}
		return reader;
	}
}
