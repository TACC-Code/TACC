class BackupThread extends Thread {
    private void writeCustomTypeMapping(PrintWriter writer) throws IOException {
        java.io.InputStream input = this.getClass().getClassLoader().getResourceAsStream("communication/customDeploy.xml.txt");
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(input));
        writeTail(reader, writer);
        reader.close();
    }
}
