package sample;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadXls {
    Workbook rwb = null;
    Cell cell = null;
    Sheet sheet;
    int rows = 0, columns = 0;
    double[][] output = new double[100][100];

    public ReadXls(){
        Read();
    }

    public void Read(){
        try {
            InputStream stream = new FileInputStream("lz01.xls");
            rwb = Workbook.getWorkbook(stream);
            sheet = rwb.getSheet(0);
            columns = sheet.getColumns();
            rows = sheet.getRows();
            output = new double[columns][rows];
            for(int i = 0; i < columns; i++){
                for(int j = 0; j < rows; j++) {
                    cell = sheet.getCell(i, j);
                    output[i][j] = Double.parseDouble(cell.getContents());
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    public void Printf(){
        for(int r = 0; r < columns; r++){
            for(int c = 0; c < rows; c++) {
                System.out.printf("%.2f ", output[r][c]);
            }
            System.out.print('\n');
        }
    }

}
