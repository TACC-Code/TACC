class BackupThread extends Thread {
    private Help(String filename) throws IOException {
        super(filename);
        this.filename = filename;
        pageType = PageType.HELP;
        windowTitle = (conf.windowtitle.length() > 0) ? conf.windowtitle : conf.getText("Help");
        printXhtmlHeader();
        InputStreamReader stream;
        if (conf.helpfile != "") stream = new InputStreamReader(new FileInputStream(conf.helpfile)); else stream = new InputStreamReader(XhtmlPageWriter.class.getResourceAsStream("resources/help" + conf.ext));
        char[] buf = new char[2048];
        int n;
        while ((n = stream.read(buf)) > 0) write(buf, 0, n);
        stream.close();
        println();
        printXhtmlFooter();
        this.close();
    }
}
