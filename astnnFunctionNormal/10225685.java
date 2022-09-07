class BackupThread extends Thread {
    public static Document loadXML(String xmlFilename, BundleContext context) {
        LogService logger = LogUtil.getLogService(context);
        Document doc = null;
        try {
            File xmlFile = context.getDataFile(xmlFilename);
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            if (xmlFile.exists()) {
                doc = docBuilder.parse(xmlFile);
            } else {
                URL resource = context.getBundle().getResource(xmlFilename);
                if (resource == null) {
                    return doc;
                }
                InputStream in = resource.openStream();
                new File(xmlFile.getParent()).mkdirs();
                xmlFile.createNewFile();
                FileOutputStream out = new FileOutputStream(xmlFile);
                int c = 0;
                while ((c = in.read()) != -1) out.write(c);
                out.flush();
                out.close();
                doc = docBuilder.parse(xmlFile);
            }
        } catch (ParserConfigurationException e) {
            logger.log(LogService.LOG_ERROR, e.getMessage(), e);
        } catch (SAXException e) {
            logger.log(LogService.LOG_ERROR, e.getMessage(), e);
        } catch (IOException e) {
            logger.log(LogService.LOG_ERROR, e.getMessage(), e);
        }
        return doc;
    }
}
