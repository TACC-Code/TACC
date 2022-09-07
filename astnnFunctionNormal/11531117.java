class BackupThread extends Thread {
    public void testSimpleTractor() {
        String file = "e:\\test\\d.xls";
        String dest = "e:\\test\\e.xls";
        try {
            Reader<Row> reader = new SimpleExcelReader(file);
            Writer writer = new SimpleExcelWriter(reader.getHeader(), dest);
            Tractor tractor = new SimpleTractor(reader, writer);
            tractor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
