class BackupThread extends Thread {
    public int CopyHTTPtoLocal(String url, String localpath) {
        InputStreamReader inFile;
        FileOutputStream outFile;
        int c;
        try {
            URL source = new URL(url);
            inFile = new InputStreamReader(source.openStream());
            outFile = new FileOutputStream(localpath);
            while ((c = inFile.read()) != -1) outFile.write(c);
            inFile.close();
            outFile.close();
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
        return 1;
    }
}
