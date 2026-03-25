package sud.enums;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryStatusTypeTest {

    @Test
    void asSelectItems() {
        List<SelectItemImpl<Integer>> items =  HistoryStatusType.asSelectItems(1);
        System.out.println(items);
        assertNotNull(items);
    }
}