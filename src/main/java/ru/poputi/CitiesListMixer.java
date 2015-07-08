package ru.poputi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class CitiesListMixer {
	
	private static String path = "C:/workspace2/poputi/src/main/resources/cities.list";
	
	public static void main(String[] args) {
		BufferedReader br = null;
		System.out.println("da");
		try {
			br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Set<String> set = new HashSet<String>();
		Set<String> resSet = new HashSet<String>();
		try {
			String line = null;
			try {
				line = br.readLine();
				while (line != null && !line.trim().isEmpty()) {
					String arr[] = line.split("=");
					set.add(arr[1].trim());
					line = br.readLine();
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String str : set) {
			//for (String sub : set) {
				//if (!str.equals(sub)) {
					resSet.add(str);
				//}
			//}
		}
		try {
 			File file = new File("c:/tmp/cities.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
 			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (String string : resSet) {
				bw.write(string + " на авто\n");				
			}
			bw.close();
 		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(resSet.size());
	}

}
