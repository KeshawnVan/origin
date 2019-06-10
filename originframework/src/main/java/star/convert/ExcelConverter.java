package star.convert;

public interface ExcelConverter<T, R> {

    ExcelValue<R> convert(T fieldValue);
}
