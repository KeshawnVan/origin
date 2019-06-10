package other;

import org.junit.Test;
import star.bean.ClassInfo;
import star.bean.UserDTO;
import star.convert.ConverterFactory;
import star.convert.ExcelConverter;
import star.convert.ExcelValue;
import star.utils.ClassUtil;
import star.utils.ExcelUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class TestExcelUtil {

    @Test
    public void testBuildColumns(){
        Map<Integer, String> integerStringMap = ExcelUtil.buildColumnInfo(UserDTO.class);
        System.out.println(integerStringMap);
    }

    @Test
    public void testGetConverter() {
        UserDTO userDTO = new UserDTO();
        userDTO.setCreateId(123L);
        ClassInfo classInfo = ClassUtil.getClassInfo(UserDTO.class);
        Field createId = classInfo.getFieldMap().get("createId");
        Class converter = ConverterFactory.getINSTANCE().getConverter(createId);
        ExcelConverter instance = (ExcelConverter)ReflectionUtil.newInstance(converter);
        Object fieldValue = ReflectionUtil.getField(createId, userDTO);
        ExcelValue excelValue = instance.convert(fieldValue);
        System.out.println(excelValue);
    }
}
