class BackupThread extends Thread {
    public void testSimpleTractor_2() {
        String file = "e:\\test\\d.xls";
        String dest = "e:\\test\\sql.sql";
        try {
            Reader<Row> reader = new SimpleExcelReader(file);
            Writer writer = new SimpleSQLWriter(reader.getHeader(), new File(dest), "table");
            Tractor tractor = new SimpleTractor(reader, writer);
            tractor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
