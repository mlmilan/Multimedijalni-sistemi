package com.example.imagegallery.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CsvFile {

	private String csv;

	
	public CsvFile() {
		csv = android.os.Environment.getExternalStorageDirectory()
				.getPath() + "/myFile.csv";
	}

	public ArrayList<String[]> readAll() {
		try {
			String[] row = null;
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			ArrayList<String[]> content = (ArrayList<String[]>) csvReader.readAll();
			 
			for (Object object : content) {
			    row = (String[]) object;
			     
			    System.out.println(row[0]
			               + " # " + row[1]);
			}
			csvReader.close();
			return content;
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return null;
	}

	public void writeAll(String filter, long time) {

		try {
			FileWriter fw = new FileWriter(csv, true);
			CSVWriter writer = new CSVWriter(fw);
			
			ArrayList<String[]> data = new ArrayList<String[]>();
			String sum_time_str = Long.toString(time);
			data.add(new String[] { filter, sum_time_str });
			writer.writeAll(data);
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}
	
	public long findAverage(String filter) {   // vraca zbir vremena izvrsavanja odgovarajuceg filtera

		long ukupno_vreme = 0;
		int broj_izvrsavanja = 0;
		try {
			String[] row = null;
			
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			System.out.println("usao");
			
			List<String[]> content = csvReader.readAll();
			 
			for (Object object : content) {
				System.out.println("usao u for");
			    row = (String[]) object;
			    
			    System.out.println(row[0] + "#" + row[1]);
			     
			    if (row[0].equals(filter)){
			    	//csvReader.close();
			    	//return row[1];
			    	ukupno_vreme += Long.parseLong(row[1]);
			    	broj_izvrsavanja++;
			    }
			}
			/*
			while((row = csvReader.readNext()) != null) {
				System.out.println("usao u while");
			    
			    System.out.println(row[0] + "#" + row[1] + "#" + row[2]);
			     
			    if (row[0].equals(filter)){
			    	csvReader.close();
			    	return row[1];
			    }
			}
			*/
			csvReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		if (broj_izvrsavanja != 0)
			return ukupno_vreme/broj_izvrsavanja;
		else return 0;
	}
	
	private String findNum(String filter) {  // vraca broj izvrsavanja odgovarajuceg filtera
		try {
			String[] row = null;
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			
			List<String[]> content = csvReader.readAll();
			 
			for (Object object : content) {
			    row = (String[]) object;
			     
			    if (row[0].equals(filter)){
			    	csvReader.close();
			    	return row[2];
			    }
			}
			csvReader.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return null;
	}

}
