class BackupThread extends Thread {
    public void getServersDetailsNew() {
        serversListTableModel.setRowCount(0);
        String cat = cmbCategory.getSelectedItem().toString();
        String loc = cmbCountry.getSelectedItem().toString();
        String type = cmbServerType.getSelectedItem().toString();
        try {
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
                String category = child.getChildText("Category");
                categoryVect.addElement(category);
                String location = child.getChildText("Location");
                locationVect.addElement(location);
                String type1 = child.getChildText("Type");
                vect.addElement("");
                vect.addElement(xml);
                if (cat.equalsIgnoreCase("All") && loc.equalsIgnoreCase("All") && type.equalsIgnoreCase("All")) serversListTableModel.addRow(vect); else if (cat.equalsIgnoreCase("All") && loc.equalsIgnoreCase("All") && !type.equalsIgnoreCase("All")) {
                    if (type.equalsIgnoreCase(type1)) serversListTableModel.addRow(vect);
                } else if (cat.equalsIgnoreCase("All") && !loc.equalsIgnoreCase("All") && type.equalsIgnoreCase("All")) {
                    if (location.equalsIgnoreCase(loc)) serversListTableModel.addRow(vect);
                } else if (!cat.equalsIgnoreCase("All") && loc.equalsIgnoreCase("All") && type.equalsIgnoreCase("All")) {
                    if (category.equalsIgnoreCase(cat)) serversListTableModel.addRow(vect);
                } else if (!cat.equalsIgnoreCase("All") && loc.equalsIgnoreCase("All") && !type.equalsIgnoreCase("All")) {
                    if (category.equalsIgnoreCase(cat) && type.equalsIgnoreCase(type1)) serversListTableModel.addRow(vect);
                } else if (!cat.equalsIgnoreCase("All") && !loc.equalsIgnoreCase("All") && type.equalsIgnoreCase("All")) {
                    if (category.equalsIgnoreCase(cat) && location.equalsIgnoreCase(loc)) serversListTableModel.addRow(vect);
                } else if (!cat.equalsIgnoreCase("All") && !loc.equalsIgnoreCase("All") && !type.equalsIgnoreCase("All")) {
                    if (category.equalsIgnoreCase(cat) && location.equalsIgnoreCase(loc) && type.equalsIgnoreCase(type1)) serversListTableModel.addRow(vect);
                }
            }
            cmbServerType.setToolTipText(cmbServerType.getSelectedItem().toString());
            cmbCategory.setToolTipText(cmbCategory.getSelectedItem().toString());
            cmbCountry.setToolTipText(cmbCountry.getSelectedItem().toString());
            cmbAttribute1.setToolTipText(cmbAttribute1.getSelectedItem().toString());
            cmbAttribute2.setToolTipText(cmbAttribute2.getSelectedItem().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
