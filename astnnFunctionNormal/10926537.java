class BackupThread extends Thread {
    protected void executeXslt(String localSurfix) throws Exception {
        String xslFileName = "resources" + localSurfix + ".xsl";
        if (!FileUtils.fileExists("target/uibuilder/" + xslFileName)) {
            File xslFileTemp = this.locator.getResourceAsFile("xsl/" + xslFileName);
            FileUtils.copyFile(xslFileTemp, new File("target/uibuilder/" + xslFileName));
        }
        File xslFile = new File("target/uibuilder/" + xslFileName);
        File srcXml = new File(this.dest, this.resourcesXml);
        String destPropertiesPath = this.dest.getAbsolutePath() + "/" + this.destPropertiesName + localSurfix + ".properties";
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
        transformer.setParameter("xdocsPath", this.dest.getAbsolutePath());
        transformer.transform(new StreamSource(srcXml), new StreamResult(destPropertiesPath));
    }
}
