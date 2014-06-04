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
	
	public void writeAllGausianBlur(String filter, long time, double sigma) {

		try {
			FileWriter fw = new FileWriter(csv, true);
			CSVWriter writer = new CSVWriter(fw);
			
			ArrayList<String[]> data = new ArrayList<String[]>();
			String sum_time_str = Long.toString(time);
			String sigma_str = Double.toString(sigma);
			data.add(new String[] { filter, sum_time_str, sigma_str });
			writer.writeAll(data);
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}
	
	public double findAverage(String filter) {   // vraca zbir vremena izvrsavanja odgovarajuceg filtera

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
			    	ukupno_vreme += Long.parseLong(row[1]);
			    	broj_izvrsavanja++;
			    }
			}
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
	
	public int findTimeGausianBlur() {   // vraca koliko puta je izvrsen gausianBlur

		int broj = 0;
		try {
			String[] row = null;
			
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			List<String[]> content = csvReader.readAll();
			 
			for (Object object : content) {
			    row = (String[]) object;
			    
			     
			    if (row[0].equals("GausianBlurFilter")){
			    	broj++;
			    }
			}
			csvReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return broj;
	}
	
	public String[] findMillisecondsGausianBlur() {   // vraca niz vremena potrebnih za sva izvrsavanja gausian blur filtera

		int broj = this.findTimeGausianBlur();
		String[] vremena = new String[broj];
		int i=0;
		try {
			String[] row = null;
			
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			List<String[]> content = csvReader.readAll();
			 
			for (Object object : content) {
			    row = (String[]) object;
			    
			     
			    if (row[0].equals("GausianBlurFilter")){
			    	vremena[i] = row[1];
			    	i++;
			    }
			}
			csvReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return vremena;
	}
	
	public String[] findSigmaGausianBlur() {   // vraca niz vrednosti sigma za sva izvrsavanja gausian blur filtera

		int broj = this.findTimeGausianBlur();
		String[] sigma = new String[broj];
		int i=0;
		try {
			String[] row = null;
			
			CSVReader csvReader = new CSVReader(new FileReader(csv));
			List<String[]> content = csvReader.readAll();
			 
			for (Object object : content) {
			    row = (String[]) object;
			    
			     
			    if (row[0].equals("GausianBlurFilter")){
			    	sigma[i] = row[2];
			    	i++;
			    }
			}
			csvReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		return sigma;
	}
	

}
