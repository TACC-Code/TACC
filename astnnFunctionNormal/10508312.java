class BackupThread extends Thread {
    public ArrayList<OceanicBuoy> getBuoyData() {
        ArrayList<OceanicBuoy> oceanicBuoys = new ArrayList<OceanicBuoy>();
        try {
            URL url = new URL(this.url);
            SAXParserFactory saxpf = SAXParserFactory.newInstance();
            SAXParser saxParser = saxpf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            OceanicBuoyDataManager dataManager = new OceanicBuoyDataManager();
            xmlReader.setContentHandler(dataManager);
            xmlReader.parse(new InputSource(url.openStream()));
            oceanicBuoys = dataManager.getBuoyDataList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oceanicBuoys;
    }
}
