package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {

	String fileName;
	BufferedReader br = null;

	/*
	 * parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */
	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		br = new BufferedReader(new FileReader(fileName));

	}

	/*
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 */
	@Override
	public Header getHeader() throws IOException {
		Header Head = null;

		br.mark(1);
		String header = br.readLine();
		String[] head = header.split(",");

		br.reset();
		Head = new Header(head);
		return Head;
	}

	/**
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. In
	 * the previous assignment, we have tried to convert a specific field value to
	 * Integer or Double. However, in this assignment, we are going to use Regular
	 * Expression to find the appropriate data type of a field. Integers: should
	 * contain only digits without decimal point Double: should contain digits as
	 * well as decimal point Date: Dates can be written in many formats in the CSV
	 * file. However, in this assignment,we will test for the following date
	 * formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm
	 * -dd')
	 */
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {

		
		DataTypeDefinitions dataTypeDef = null;
		String[] data = null;

		br.mark(1);
		br.readLine();

		String line2 = br.readLine();

		br.reset();

		line2 += " ,";
		data = line2.split(",");
		for (String a : data) {
			System.out.println(a);
		}
		if (data != null) {
			for (int i = 0; i < data.length; i++) {

				String reg = "\\d+";
			
				String dateReg = "^\\d{4}-\\d{2}-\\d{2}$|^d{2}/\\d{2}/\\d{4}$|\\^d{2}-\\d{2}-\\d{4}$";
				
				if ((data[i].matches(reg))) {
					int a = Integer.parseInt(data[i]);
					String s = ((Object) a).getClass().getName();
					data[i] = s;
				} else if ((data[i].matches(dateReg))) {
					Date thisDate;
					DateFormat dateNeed;
					try {
						dateNeed = new SimpleDateFormat("yyyy-MM-dd");
						thisDate = (Date) dateNeed.parse(data[i]);
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						System.out.println(formatter.format(thisDate));
						String s = ((Object) thisDate).getClass().getName();
						data[i] = s;
						
					} catch (ParseException e) {
						e.printStackTrace();
						System.out.println("error");
					} 
				} else if (data[i].matches(" ")) {
					Object obj = new Object();
					String s = ((Object) obj).getClass().getName();
					data[i] = s;
				} else {
					String s = (data[i]).getClass().getName();
					data[i] = s;
				}

				
			}
		}

		
		dataTypeDef = new DataTypeDefinitions(data);
		return dataTypeDef;

	}

}