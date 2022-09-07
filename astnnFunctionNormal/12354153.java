class BackupThread extends Thread {
    public XMLWriter(XMLReader xmlreader, Writer writer) {
        super(xmlreader);
        init(writer);
    }
}
