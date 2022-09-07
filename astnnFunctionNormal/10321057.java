class BackupThread extends Thread {
    public static String transformString(String xslFilePath, String xmlContent) {
        try {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xslFilePath));
            StringWriter swriter = new StringWriter();
            StringReader sreader = new StringReader(xmlContent);
            transformer.transform(new StreamSource(sreader), new StreamResult(swriter));
            return swriter.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
