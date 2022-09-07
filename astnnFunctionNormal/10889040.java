class BackupThread extends Thread {
    public java.util.Vector getPhotos(org.jdom.Document doc) {
        java.util.Vector v1 = new java.util.Vector();
        java.util.Hashtable ht1 = new java.util.Hashtable();
        utility = ejb.bprocess.util.Utility.getInstance(null);
        homeFactory = ejb.bprocess.util.HomeFactory.getInstance();
        newGenXMLGenerator = ejb.bprocess.util.NewGenXMLGenerator.getInstance(null);
        String xmlstr = (new org.jdom.output.XMLOutputter()).outputString(doc);
        java.util.Hashtable ht = newGenXMLGenerator.parseXMLDocument(xmlstr);
        String libid = ht.get("LibraryID").toString();
        String patid = ht.get("PatronId").toString();
        String fileSeperator = System.getProperties().get("file.separator").toString();
        try {
            java.io.File patpho = new java.io.File(ejb.bprocess.util.NewGenLibRoot.getRoot() + "/PatronPhotos/" + "LIB_" + libid + "/" + "PAT_" + patid + ".jpg");
            System.out.println("patronId : " + patid);
            v1.addElement("PAT_" + patid + ".jpg");
            java.nio.channels.FileChannel fc = (new java.io.FileInputStream(patpho)).getChannel();
            int fileLength = (int) fc.size();
            System.out.println("fileLength : " + fileLength);
            java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
            byte[] byx = new byte[bb.capacity()];
            System.out.println(byx.length);
            System.out.println(bb.hasArray());
            fc.close();
            bb.get(byx);
            System.out.println(byx.length);
            v1.addElement(byx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v1;
    }
}
