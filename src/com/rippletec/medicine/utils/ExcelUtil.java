package com.rippletec.medicine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

import com.rippletec.medicine.bean.PageBean;
import com.rippletec.medicine.dao.BackGroundMedicineTypeDao;
import com.rippletec.medicine.model.BackGroundMedicineType;
import com.rippletec.medicine.model.ChineseMedicine;
import com.rippletec.medicine.model.Medicine;
import com.rippletec.medicine.model.MedicineType;
import com.rippletec.medicine.model.WestMedicine;
import com.rippletec.medicine.service.BackGroundMedicineTypeManager;
import com.rippletec.medicine.service.ChineseMedicineManager;
import com.rippletec.medicine.service.MedicineTypeManager;
import com.rippletec.medicine.service.WestMedicineManager;


@Repository(ExcelUtil.NAME)
public class ExcelUtil {
    
    public static final String NAME = "Excelutil";

    private  String excelPath = "";
    private  String sheetName = "";
    
    @Resource(name = MedicineTypeManager.NAME)
    private  MedicineTypeManager medicineTypeManager; 
    
    @Resource(name = WestMedicineManager.NAME)
    private WestMedicineManager westMedicineManager;
    
    @Resource(name = ChineseMedicineManager.NAME)
    private ChineseMedicineManager chineseMedicineManager;
    
    @Resource(name = BackGroundMedicineTypeManager.NAME)
    private BackGroundMedicineTypeManager backGroundMedicineTypeManager;
    

    public ExcelUtil() {
    }
    
    public ExcelUtil(String excelPath, String sheetName) {
	super();
	this.excelPath = excelPath;
	this.sheetName = sheetName;
    }


    public  XSSFWorkbook getExcelDom(String path) throws FileNotFoundException, IOException {
	return (new XSSFWorkbook(new FileInputStream(new File(path))));
    }
    
    
    public  List<ChineseMedicine> getChineseModels() {
	return null;
    }
    
    public  List<WestMedicine> getWestModels() {
	List<WestMedicine> westMedicines = new LinkedList<WestMedicine>();
	XSSFWorkbook xssfWorkbook;
	try {
	    xssfWorkbook = getExcelDom(excelPath);
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
	XSSFSheet xssfSheet = xssfWorkbook.getSheet(sheetName);
	Iterator<Row> rowIterator = xssfSheet.iterator();
	while (rowIterator.hasNext()) {
	    Row row = rowIterator.next();
	
	}
	return null;
    }
    
    public  boolean setWestTypeToDatabase() {
	XSSFWorkbook xssfWorkbook;
	try {
	    xssfWorkbook = getExcelDom(excelPath);
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	int bigTypeId = medicineTypeManager.save(new MedicineType("西药", MedicineType.DEFAULT_PARENT_ID, MedicineType.WEST));
	XSSFSheet xssfSheet = xssfWorkbook.getSheet(sheetName);
	
	Iterator<Row> rowIterator = xssfSheet.iterator();
	while (rowIterator.hasNext()) {
	    Row row = rowIterator.next();
	    int parent_id = bigTypeId;
	    BackGroundMedicineType backGroundMedicineType = new BackGroundMedicineType();
	    backGroundMedicineType.setType(BackGroundMedicineType.TYPE_NORMAL);
	    backGroundMedicineType.setFirstType("西药");
	    backGroundMedicineType.setFirstType_id(bigTypeId);
	    for (int i = 1; i < 4; i++) {
		String typeString = getCellString(row, i);
		if (!StringUtil.hasText(typeString)) {
		    typeString = "其他";
		    parent_id = medicineTypeManager.uniqueSave(new MedicineType(typeString, parent_id, MedicineType.WEST));
		    backGroundMedicineType.setMedicineType(i+1, parent_id, typeString);
		    break;
		}
		MedicineType medicineType = new MedicineType(typeString, parent_id, MedicineType.WEST);
		MedicineType mType = medicineTypeManager.isExist(medicineType);
		if(mType != null){
		    parent_id = mType.getId();
		    backGroundMedicineType.setMedicineType(i+1, parent_id, typeString);
		    continue;
		}
		parent_id = medicineTypeManager.uniqueSave(medicineType);
		backGroundMedicineType.setMedicineType(i+1, parent_id, typeString);
	    }
	    if (row.getCell(18) == null || row.getCell(17)==null || row.getCell(16) == null)
		continue;
	    MedicineType medicineType = medicineTypeManager.find(parent_id);
	    if(medicineType.getBackGroundMedicineType() == null){
		medicineType.setBackGroundMedicineType(backGroundMedicineType);
		medicineTypeManager.update(medicineType);
	    }
	    Medicine medicine = new Medicine(Medicine.WEST);
	    WestMedicine westMedicine = 
			new WestMedicine(medicine, medicineTypeManager.find(parent_id), 
				getCellString(row, 4), getCellString(row, 5), getCellString(row, 6),
				getCellString(row, 7), getCellString(row, 8), getCellString(row, 9),
				getCellString(row, 10), getCellString(row, 11), getCellString(row, 12),
				getCellString(row, 13), getCellString(row, 14), getCellString(row, 15),
				getCellString(row, 16), getCellString(row, 17), getCellString(row, 18),
				getCellString(row, 18), 1.00, WestMedicine.ON_PUBLISTH, "sortkey");
	    westMedicineManager.save(westMedicine);
	    
	}
	System.out.println(xssfSheet.getLastRowNum());
   	return true;
    }
    
    public  boolean setChineseTypeToDatabase() {
	XSSFWorkbook xssfWorkbook;
	try {
	    xssfWorkbook = getExcelDom(excelPath);
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	int bigTypeId = medicineTypeManager.save(new MedicineType("中药", MedicineType.DEFAULT_PARENT_ID, MedicineType.CHINESE));
	
	XSSFSheet xssfSheet = xssfWorkbook.getSheet(sheetName);	
	Iterator<Row> rowIterator = xssfSheet.iterator();
	while (rowIterator.hasNext()) {
	    ChineseMedicine chineseMedicine = new ChineseMedicine();
	    Row row = rowIterator.next();
	    int parent_id = bigTypeId;
	    for (int i = 0; i < 3; i++) {
		String typeString = getCellString(row, i);
		if (!StringUtil.hasText(typeString)) {
		    typeString = "其他";
		    parent_id = medicineTypeManager.uniqueSave(new MedicineType(typeString, parent_id, MedicineType.CHINESE));
		    break;
		}
		MedicineType medicineType = new MedicineType(typeString, parent_id, MedicineType.CHINESE);
		MedicineType mType = medicineTypeManager.isExist(medicineType);
		if(mType != null){
		    parent_id = mType.getId();
		    continue;
		}
		parent_id = medicineTypeManager.uniqueSave(medicineType);
	    }
	    
	    MedicineType type =  medicineTypeManager.find(parent_id);
	    chineseMedicine.setName(getCellString(row, 4));
	    chineseMedicine.setContent(getCellString(row, 5));
	    chineseMedicine.setEfficacy(getCellString(row, 6));
	    chineseMedicine.setAnnouce(getCellString(row, 7));
	    chineseMedicine.setManual(getCellString(row, 8));
	    chineseMedicine.setPreparations(getCellString(row, 9));
	    chineseMedicine.setStore(getCellString(row, 10));
	    chineseMedicine.setCategory(getCellString(row, 11));
	    chineseMedicine.setMedicineType(type);
	    Medicine medicine = new Medicine(Medicine.CHINESE);
	    chineseMedicine.setMedicine(medicine);
	    chineseMedicine.setPrice(1.00);
	    chineseMedicine.setSortKey("sorkey");
	    chineseMedicine.setStatus(ChineseMedicine.ON_PUBLISTH);
	    chineseMedicineManager.save(chineseMedicine);
	}
	System.out.println(xssfSheet.getLastRowNum());
   	return true;
    }
    
   
    
    
    public  String getCellString(Row row , int index) {
	Cell cell = row.getCell(index);
	if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING) 
	    return "";
	return cell.getStringCellValue();
    }

    public String getExcelPath() {
        return excelPath;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelUtil setExcelPath(String excelPath) {
        this.excelPath = excelPath;
        return this;
    }

    public ExcelUtil setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }
    
    
    

}
