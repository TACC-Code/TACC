class BackupThread extends Thread {
    public void persistRecords(String alreadyExecutedId, String thisLibraryUCID, String ucServerURL, String localhostServer, java.awt.Component comp, int totalCount) {
        setInitialUploadStatus(0);
        this.comp = comp;
        this.ucServerURL = ucServerURL;
        CATALOGUERECORD_MANAGER catmanager = new CATALOGUERECORD_MANAGER();
        try {
            String query = "select cataloguerecordid, bibiliographic_level_id, material_type_id from cataloguerecord where cataloguerecordid>" + alreadyExecutedId;
            java.sql.Connection con = reports.utility.database.PostgresConnectionPool.getInstance().getConnection();
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next() && isProcess() == true) {
                System.out.println("inside while loop");
                setTotalCountStatus(getTotalCountStatus() + 1);
                setCurrentRecordStatus(0);
                String entryId = "1";
                String entryLibId = thisLibraryUCID;
                String ownerLibId = thisLibraryUCID;
                String libraryId = "1";
                String holdingLibId = thisLibraryUCID;
                String biblevel = rs.getString(2);
                String mattypeid = rs.getString(3);
                String catrecid = rs.getString(1);
                setCurrentRecordStatus(getCurrentRecordStatus() + 1);
                String urlStr = "http://" + localhostServer + ":8080/newgenlibctxt/oai2.0?verb=GetRecord&metadataPrefix=marc21&identifier=CAT";
                urlStr += "_" + catrecid + "_1";
                URL url = new URL(urlStr);
                URLConnection urlcon = url.openConnection();
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            comp.setEnabled(true);
            setInitialUploadStatus(1);
        } catch (Exception expu) {
            expu.printStackTrace();
            comp.setEnabled(true);
            setInitialUploadStatus(2);
        }
    }
}
