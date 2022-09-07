class BackupThread extends Thread {
    public void setData(Reader reader, Reader reader1, Writer writer) {
        setXMLInput(reader);
        setXSLInput(reader1);
        setOutput(writer);
    }
}
