/**
 *  zuständig für das schreiben einer Csv-Datei
 */
package edu.kit.iti.formal.mandatsverteilung.exporter;



import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author Pascal
 * 
 */
public class CSVDateiWriter {
	/**
	 * CSVWriter zum schreiben von CSV-Dateien. 
	 */
	private CSVWriter writer;
	
	/**
	 * Der Name der zu erzeugenden Datei. 
	 */
	private String filename;
	
	/**
	 *  Konstante um alle anführungszeichen im CSV-Writer zu unterdrücken.
	 */
	private char NO_QUOTE_CHARACTER;

	public CSVDateiWriter(String filename) {
		this.filename = filename;
	}
	
	/**
	 * schreibt eine CSV-Datei, wird von den verschiedenen exportern aufgerufen.
	 */
	protected void schreibeDatei(ArrayList<String[]> daten)  {
		try {
			this.writer = new CSVWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(this.filename), "ISO-8859-1")), ';', NO_QUOTE_CHARACTER);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		this.writer.writeAll(daten);
		try {
			this.writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
