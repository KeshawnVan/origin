package star.convert;

import star.utils.DateUtil;

import java.util.Date;

public class String2DateConverter implements ExcelConverter<String, Date> {
    @Override
    public ExcelValue<Date> convert(String fieldValue) {
        ExcelValue<Date> excelValue = new ExcelValue<>();
        excelValue.setType("Date");
        excelValue.setValue(DateUtil.toUtilDate(fieldValue));
        return excelValue;
    }
}
