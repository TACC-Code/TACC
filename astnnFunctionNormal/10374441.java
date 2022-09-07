class BackupThread extends Thread {
    public void parse(URL url) {
        InputStream is;
        try {
            is = url.openStream();
            this.reader = new BufferedReader(new InputStreamReader(is));
            while (getNextStatement()) ;
            reader.close();
            is.close();
        } catch (Exception ex) {
            System.out.println("CSSParser.parse: " + ex);
            ex.printStackTrace();
        }
    }
}
