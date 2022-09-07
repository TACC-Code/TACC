class BackupThread extends Thread {
    public void getServersDetails() {
        try {
            locationVect = new Vector();
            categoryVect = new Vector();
            String fileName = System.getProperty("user.home");
            fileName = fileName.concat("/NGLZ3950Servers.xml");
            File inFile = new File(fileName);
            FileInputStream fis = new FileInputStream(inFile);
            FileChannel inChannel = fis.getChannel();
            ByteBuffer buf = ByteBuffer.allocate((int) inChannel.size());
            inChannel.read(buf);
            inChannel.close();
            String myString = new String(buf.array());
            org.jdom.Element root = nglXMLUtility.getRootElementFromXML(myString);
            java.util.List clist = root.getChildren();
            for (int i = 0; i < clist.size(); i++) {
                java.util.Vector vect = new java.util.Vector();
                org.jdom.Element child = (org.jdom.Element) clist.get(i);
                String xml = nglXMLUtility.generateXML((org.jdom.Element) child.clone());
                vect.addElement(new Boolean(false));
                vect.addElement(child.getChildText("Name"));
                categoryVect.addElement(child.getChildText("Category"));
                locationVect.addElement(child.getChildText("Location"));
                vect.addElement("");
                vect.addElement(xml);
                serversListTableModel.addRow(vect);
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
