import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class WriteExcel {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	private String outputFile;
	private String resultsAvgFile;
	
	private Hashtable<Integer,Double> hash = new Hashtable<Integer, Double>();
	
public void setOutputFile(String inputFile) {
	this.inputFile = inputFile;
	}

public void setInputFile(String outputFile) {
	this.outputFile = outputFile;
	}

	public void write() throws IOException, WriteException {
		//--------------------------
		
		File fileA = new File(resultsAvgFile);
		WorkbookSettings wbSettingsA = new WorkbookSettings();

		wbSettingsA.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbookA = Workbook.createWorkbook(fileA, wbSettingsA);
		
		workbookA.createSheet("avg", 0);
		WritableSheet excelSheetA = workbookA.getSheet(0);
		createLabel(excelSheetA);
		
		//--------------------------
		
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		
		String [] dirs = new File(outputFile).list();
		
		int index=-1;
		
		for(int i=0; i<dirs.length;i++){
			if(dirs[i].startsWith("core")){
				index++;
				workbook.createSheet(dirs[i], index);
				WritableSheet excelSheet = workbook.getSheet(index);
				createLabel(excelSheet);
				createContent(excelSheet,outputFile+"/"+dirs[i],excelSheetA,2,index+2);
			}

		}
		workbookA.write();
		workbookA.close();
		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet)
			throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);
		

	}

	private void createContent(WritableSheet sheet, String dir, WritableSheet avgSheet,int avgC, int avgR) throws WriteException,
			RowsExceededException {
		try {
			FileInputStream fstream = new FileInputStream(dir);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			int col=1;
			int ctr=1;
			
			int scol=0,srow=0;
			
			boolean flag1=false, flag=false;
			int temp=0;
		    double tempV=0;
		    
		    int x = new Integer(avgC);
		    
			while ((line =  br.readLine()) != null){
				if(flag1){
					temp = Integer.parseInt((line));
				}
				flag1=false;
				
				/*--------*/
				if(line.equals("")){
					flag1=true;
					if(flag){
						addNumber(avgSheet,x++,avgR,tempV/(ctr-2));
					}
					tempV=0;
					col++;
					ctr=1;
					continue;
				}
				flag = true;
				
				tempV += Double.parseDouble((line));
				addNumber(sheet, col, ctr, Double.parseDouble((line)));
				ctr++;
			}
			addNumber(avgSheet, x++,avgR, (tempV/(ctr-2)));
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			Double integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer);
		sheet.addCell(number);
	}

	public static void main(String[] args) throws WriteException, IOException {
		WriteExcel test = new WriteExcel();
		
		String dir = "./".replace("\\", "/");
		
		test.setInputFile(dir);
		test.setOutputFile("./results.xls");
		
		test.resultsAvgFile = "./resultsAvg.xls";
		
		test.write();
		System.out.println("done");
	}
}