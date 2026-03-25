package sud.excel;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.function.LongConsumer;

@Service
public class ExportService {


    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/";
    private static final int SPLIT_SIZE = 1_000_000; //Excel 2003 allows is

    public <T> File export(Iterable<T> iterable, String fileName, List<ColumnDefinition> def, List<ExportColumnSetting> exportColumnSettings, LongConsumer statusConsumer) throws IOException {
        File file = new File(TEMP_DIR + fileName + "-" + System.currentTimeMillis() + ".xlsx");
        return doExport(iterable, def, exportColumnSettings, statusConsumer, file);
    }

    public <T> byte[] exportAsByteArray(Iterable<T> iterable, String fileName, List<ExportColumnSetting> exportColumnSettings) throws IOException {
        return convertFileToByteArray(export(iterable, fileName, null,  exportColumnSettings, null));
    }

    private static byte[] convertFileToByteArray(File file) throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] byteArray = new byte[(int) file.length()];
            fileInputStream.read(byteArray);
            return byteArray;
        }
    }

    private <T> File doExport(Iterable<T> iterable, List<ColumnDefinition> def, List<ExportColumnSetting> exportColumnSettings, LongConsumer statusConsumer, File file) throws IOException, IOException, IOException {
        ExcelExportManager excel = ExcelExportManager.create();
        Iterator<T> iterator = iterable.iterator();
        if (def == null) {
            Iterator<T> iterator2 = iterable.iterator();
            T next = iterator2.next();
            def = createColumnDefinitionBySettings(next, exportColumnSettings);
        }
        int i = 0;
        while (iterator.hasNext()) {
            excel.addSheet("Data " + ++i, iterator, def, SPLIT_SIZE);
        }
        excel.build(file);
        return file;
    }

    public List<ColumnDefinition> createColumnDefinitionBySettings(Object inputObj, List<ExportColumnSetting> exportColumnSettings) {
        final List<ColumnDefinition> res = new ArrayList<>();
        exportColumnSettings.forEach(ecs -> {
            ColumnValueGetter getter = findGetter(inputObj, ecs.getName());
            if (getter != null) {
                ColumnDefinition columnDefinition = new ColumnDefinition(ecs.getLabel(), null, getter, ecs);
                res.add(columnDefinition);
            }
        });
        return res;
    }

    public <T> File exportWithStaticName(Iterable<T> iterable, String fileName, List<ColumnDefinition> def, List<ExportColumnSetting> exportColumnSettings, LongConsumer statusConsumer) throws IOException {
        File file = new File(TEMP_DIR + fileName + ".xlsx");
        return doExport(iterable, def, exportColumnSettings, statusConsumer, file);

    }


    public ColumnValueGetter findGetter(Object obj, String field) { //NOSONAR
        if (field == null) {
            return null;
        }
        List<ColumnValueGetter> getters = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        for (String fieldName : field.split("\\.")) {
            Method m;
            if (obj.getClass().isRecord()) {
                m = Arrays.stream(clazz.getRecordComponents()).filter(rc -> rc.getName().equals(fieldName)).findFirst().map(RecordComponent::getAccessor).orElse(null);
            } else {
                m = getMethodForUsualClass(clazz, fieldName);
            }
            if (m != null && (!Modifier.isPublic(m.getModifiers()) || m.getReturnType().equals(void.class))) {
                m = null;
            }
            if (m != null) {
                clazz = m.getReturnType();
                ColumnValueGetter getter = new ColumnValueGetter(m);
                getters.add(getter);
                continue;
            }
            Field f = null;
            try {
                f = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) { //NOSONAR

            }
            if (f == null || !Modifier.isPublic(f.getModifiers())) {
                return null;
            }
            clazz = f.getType();
            ColumnValueGetter getter = new ColumnValueGetter(f);
            getters.add(getter);
        }
        ColumnValueGetter prev = null;
        Collections.reverse(getters);
        for (ColumnValueGetter getter : getters) {
            getter.setFieldValueGetter(prev);
            prev = getter;
        }
        return getters.stream().findFirst().orElse(null);
    }

    private Method getMethodForUsualClass(Class<?> clazz, String fieldName) {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod("get" + capitalizeFirstLetter(fieldName));
        } catch (NoSuchMethodException ignored) { //NOSONAR

        }
        if (m == null) {
            try {
                m = clazz.getDeclaredMethod("is" + capitalizeFirstLetter(fieldName));
            } catch (NoSuchMethodException ignored) { //NOSONAR

            }
        }
        return m;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }


}
