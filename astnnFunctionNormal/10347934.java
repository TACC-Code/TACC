class BackupThread extends Thread {
    public TestXMLDB() throws Exception {
        db = XMLDBLight.createReadWriteDB(".", "testdb");
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fac.newDocumentBuilder();
        this.document = builder.newDocument();
        Thread t = new Thread(new writer());
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
}
