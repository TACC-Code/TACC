class BackupThread extends Thread {
    public void list() throws IOException, TransformerConfigurationException {
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        Templates xslt = loadDcmDirXSL(tf);
        DirReader reader = fact.newDirReader(dirFile);
        reader.getFileSetInfo().writeFile2(getTransformerHandler(tf, xslt, FILE_SET_INFO), dict, null, 128, null);
        try {
            list("", reader.getFirstRecord(onlyInUse), tf, xslt);
        } finally {
            reader.close();
        }
    }
}
