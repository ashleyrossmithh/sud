package sud.excel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sud.SudTestConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = SudTestConfiguration.class)
@EnableWebSecurity
class ExportServiceTest {

    @Autowired
    ExportService service;



    @BeforeEach
    void setUp() {
        Mockito.mock(ColumnDefinition.class);
    }

    @Test
    void exportWithStaticName() throws IOException {
        Field fieldMock = Mockito.mock(Field.class);

        ColumnDefinition columnDefinition = new ColumnDefinition("имя", "стиль", new ColumnValueGetter(fieldMock));


        Iterable<CellTestValue> it = Mockito.mock(Iterable.class);

        List<CellTestValue> items = List.of(new CellTestValue("желтый", 12L), new CellTestValue("красный", 22L));
        Mockito.when(it.iterator()).thenReturn(items.iterator());

        ExportColumnSetting setting1 = Mockito.mock(ExportColumnSetting.class);
        Mockito.when(setting1.getName()).thenReturn("color");
        Mockito.when(setting1.getLabel()).thenReturn("Цвет");

        ExportColumnSetting setting2 = Mockito.mock(ExportColumnSetting.class);
        Mockito.when(setting2.getName()).thenReturn("weight");
        Mockito.when(setting2.getLabel()).thenReturn("Вес");

        File f = service.export(it, "test", null, List.of(setting1, setting2), null);
        assertNotNull(f);
    }
}