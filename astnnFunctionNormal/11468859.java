class BackupThread extends Thread {
    public static void main(String[] args) {
        SECFileReader readFile = new SECFileReader("C:\\thom\\projects\\cartography\\fornast.sec");
        AccessXMLFile writeFile = new AccessXMLFile("C:\\thom\\projects\\cartography\\fornast.xml");
        try {
            readFile.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            writeFile.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
