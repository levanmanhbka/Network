package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MFileIO {
	public static final String ORIGINAL_PATH = "D:\\Network\\data";

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

	public void SaveListUser(ArrayList<MUser> users) {
		String content = "";
		for (int i = 0; i < users.size(); i++) {
			content += users.get(i).name + "\n" + users.get(i).password + "\n";
		}
		WriteToFile(content, ORIGINAL_PATH, "users.txt");
	}
	
	public ArrayList<MUser> getListUsers(){
		ArrayList<MUser> users = new ArrayList<>();
		BufferedReader reader = ReadFromFile(ORIGINAL_PATH, "users.txt");
		String str;
		try {
			while ((str = reader.readLine()) != null) {
				String name = str;
				String password = reader.readLine();
				MUser user = new MUser(name, password);
				users.add(user);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
}
