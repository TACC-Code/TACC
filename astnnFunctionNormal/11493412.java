class BackupThread extends Thread {
    public void genereMetaData() {
        try {
            URL url = new URL("http://localhost:8080/pentaho/AdhocWebService?userid=" + userName + "&password=" + password + "&model=" + modelName + "&component=getbusinessmodel&domain=" + domain + "/metadata.xmi");
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList viewNodeLst = doc.getElementsByTagName("view");
            String view_id = null;
            int nbBusinessColumns = getNbBusinessColumns(doc);
            pentahoModelColumnNames = new String[nbBusinessColumns];
            pentahoModelViewNames = new String[nbBusinessColumns];
            pentahoModelColumnLabels = new String[nbBusinessColumns];
            pentahoModelColumnTypes = new String[nbBusinessColumns];
            int colCount = 0;
            for (int s = 0; s < viewNodeLst.getLength(); s++) {
                Node viewNode = viewNodeLst.item(s);
                if (viewNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fstElmnt = (Element) viewNode;
                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("view_id");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    view_id = (String) ((Node) fstNm.item(0)).getNodeValue();
                }
                NodeList columnNodeList = viewNode.getChildNodes();
                for (int t = 0; t < columnNodeList.getLength(); t++) {
                    Node columnNode = columnNodeList.item(t);
                    if (columnNode.getNodeName().equals("column")) {
                        Element colAElmnt = (Element) columnNode;
                        NodeList colANmElmntLst = colAElmnt.getElementsByTagName("column_id");
                        Element colANmElmnt = (Element) colANmElmntLst.item(0);
                        NodeList colANm = colANmElmnt.getChildNodes();
                        pentahoModelColumnNames[colCount] = ((Node) colANm.item(0)).getNodeValue();
                        Element colBElmnt = (Element) columnNode;
                        NodeList colBNmElmntLst = colBElmnt.getElementsByTagName("column_name");
                        Element colBNmElmnt = (Element) colBNmElmntLst.item(0);
                        NodeList colBNm = colBNmElmnt.getChildNodes();
                        pentahoModelColumnLabels[colCount] = ((Node) colBNm.item(0)).getNodeValue();
                        Element colCElmnt = (Element) columnNode;
                        NodeList colCNmElmntLst = colCElmnt.getElementsByTagName("column_type");
                        Element colCNmElmnt = (Element) colCNmElmntLst.item(0);
                        NodeList colCNm = colCNmElmnt.getChildNodes();
                        pentahoModelColumnTypes[colCount] = ((Node) colCNm.item(0)).getNodeValue();
                        pentahoModelViewNames[colCount] = view_id;
                        colCount++;
                    }
                }
            }
            for (int i = 0; i < pentahoModelColumnNames.length; i++) {
                System.out.println(pentahoModelColumnNames[i] + "|" + pentahoModelColumnLabels[i] + "|" + pentahoModelColumnTypes[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
