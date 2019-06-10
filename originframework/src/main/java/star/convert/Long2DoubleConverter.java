package star.convert;

import star.utils.CastUtil;

public class Long2DoubleConverter implements ExcelConverter<Long, Double> {
    @Override
    public ExcelValue<Double> convert(Long fieldValue) {
        ExcelValue<Double> excelValue = new ExcelValue<>();
        excelValue.setType("double");
        excelValue.setValue(CastUtil.castDouble(fieldValue));
        return excelValue;
    }
}
