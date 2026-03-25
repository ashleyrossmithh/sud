package sud.excel;

public class ColumnDefinition {
    private String name;
    private String style;
    private ColumnValueGetter columnValueGetter;
    private ExportColumnSetting exportColumnSetting;

    public ColumnDefinition(String name, String style, ColumnValueGetter columnValueGetter) {
        this.name = name;
        this.style = style;
        this.columnValueGetter = columnValueGetter;
    }

    public ColumnDefinition(String name, String style, ColumnValueGetter columnValueGetter, ExportColumnSetting exportColumnSetting) {
        this(name, style, columnValueGetter);
        this.exportColumnSetting = exportColumnSetting;
    }

    public String getName() {
        return name;
    }

    public String getLabelOrName() {
        if (exportColumnSetting == null)
            return this.name;
        return exportColumnSetting.getLabel() == null ? this.name : exportColumnSetting.getLabel();

    }

    public ColumnValueGetter getColumnValueGetter() {
        return columnValueGetter;
    }

    public ExportColumnSetting getExportColumnSetting() {
        return exportColumnSetting;
    }

    public String getStyle() {
        return style;
    }

    public Object get(Object rowObj) {
        try {
            return columnValueGetter.get(rowObj);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
