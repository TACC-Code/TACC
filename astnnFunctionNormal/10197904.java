class BackupThread extends Thread {
    protected void assembleEAR(PluginDescriptor pd, String earFilename, File destDir) throws IOException, XMLStreamException {
        File localDestDir = new File(getPluginTmpDir(), pd.getId());
        localDestDir.mkdirs();
        File srcXML = new File(localDestDir, "META-INF/application.xml");
        String templateFilename = getDescriptor().getAttribute(ATTRIBUTE_TEMPLATE_APPXML).getValue();
        File templateFile = getFilePath(templateFilename);
        if (!templateFile.exists()) {
            throw new RuntimeException("Could not locate: '" + templateFile.getPath() + "' in " + getDescriptor().getId());
        }
        srcXML.getParentFile().mkdirs();
        logger.debug("Copy " + templateFile + " to " + srcXML);
        FileUtils.copyFile(templateFile, srcXML);
        assemblerMetaInf(pd, localDestDir);
        assemblerMetaInfAdaptors(pd, localDestDir);
        StringBuffer originalXML = new StringBuffer();
        originalXML.append(FileUtils.readFileToString(srcXML));
        String xslt = getXSLT(pd, localDestDir);
        File applicationxmlXSLT = new File(getPluginTmpDir(), "applicationxml-xslt.xml");
        logger.debug("Write application.xml XSLT to " + applicationxmlXSLT.getPath());
        FileUtils.writeStringToFile(applicationxmlXSLT, xslt);
        String translatedXMLString = getTranslatedXML(originalXML.toString(), xslt);
        srcXML.getParentFile().mkdirs();
        logger.debug("Write translated application.xml to " + srcXML.getPath());
        FileUtils.writeStringToFile(srcXML, translatedXMLString);
        buildEAR(earFilename, localDestDir, destDir);
    }
}
