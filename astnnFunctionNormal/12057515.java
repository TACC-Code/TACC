class BackupThread extends Thread {
    private File URLDownloadToFile(String url) throws IOException {
        String fileName = System.getProperty("catalina.home") + System.getProperty("file.separator") + "webapps/gps/xml/tmp.xml";
        BufferedInputStream bis = new BufferedInputStream((new URL(url)).openConnection().getInputStream());
        int c;
        StringBuffer sb = new StringBuffer();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        FileWriter fw = new FileWriter(fileName);
        fw.write(sb.toString());
        bis.close();
        fw.flush();
        fw.close();
        return new File(fileName);
    }
}
