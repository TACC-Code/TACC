class BackupThread extends Thread {
    public XmlWriter(XMLReader xmlreader, Writer writer) {
        super(xmlreader);
        init(writer);
    }
}
