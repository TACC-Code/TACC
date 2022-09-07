class BackupThread extends Thread {
    public void persistRecordsSelectiveMode(String thisLibraryUCID, String ucServerURL, String from, String to, String localhostServer, java.awt.Component comp) {
        this.comp = comp;
        setSelectiveUploadStatus(false);
        this.ucServerURL = ucServerURL;
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        CATALOGUERECORD_MANAGER catmanager = new CATALOGUERECORD_MANAGER();
        List list = catmanager.getCatalogueRecordsBetweenDates(session, from, to);
        for (int i = 0; i < list.size(); i++) {
            if (isProcess() == true) {
                setTotalCountStatus(getTotalCountStatus() + 1);
                setCurrentRecordStatus(0);
                CATALOGUERECORD catrec = (CATALOGUERECORD) list.get(i);
                String entryId = "1";
                String entryLibId = thisLibraryUCID;
                String ownerLibId = thisLibraryUCID;
                String libraryId = "1";
                String holdingLibId = thisLibraryUCID;
                String biblevel = catrec.getBibiliographic_level_id().toString();
                String mattypeid = catrec.getMaterial_type_id().toString();
                String catrecid = catrec.getPrimaryKey().getCataloguerecordid().toString();
                setCurrentRecordStatus(getCurrentRecordStatus() + 1);
                String urlStr = "http://" + localhostServer + ":8080/newgenlibctxt/oai2.0?verb=GetRecord&metadataPrefix=marc21&identifier=CAT";
                urlStr += "_" + catrecid + "_1";
                try {
                    URL url = new URL(urlStr);
                    URLConnection urlcon = url.openConnection();
                    InputStream is = urlcon.getInputStream();
                    SAXBuilder sb = new SAXBuilder();
                    Document doc = sb.build(is);
                    setCurrentRecordStatus(getCurrentRecordStatus() + 1);
                    Hashtable hta = new Hashtable();
                    hta.put("USE_CAT_ID", catrecid);
                    hta.put("entryId", entryId);
                    hta.put("entryLibId", entryLibId);
                    hta.put("ownerLibId", ownerLibId);
                    hta.put("libraryId", libraryId);
                    hta.put("holdingLibId", holdingLibId);
                    hta.put("bibliographiclevel", biblevel);
                    hta.put("materialType", mattypeid);
                    setCurrentRecordStatus(getCurrentRecordStatus() + 1);
                    sendRecordToUCSever(hta, doc);
                } catch (Exception expu) {
                    expu.printStackTrace();
                }
            }
        }
        session.close();
        setSelectiveUploadStatus(true);
        comp.setEnabled(true);
    }
}
