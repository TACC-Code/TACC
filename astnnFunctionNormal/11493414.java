class BackupThread extends Thread {
    public String[][] getPentahoResultSet(String targetUrl, int nbColumns) {
        String[][] rowSet = null;
        try {
            URL url = new URL(targetUrl);
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList dataRowNodeList = doc.getElementsByTagName("DATA-ROW");
            nbRowsPentahoResultSet = dataRowNodeList.getLength();
            rowSet = new String[dataRowNodeList.getLength()][nbColumns];
            for (int rowNum = 0; rowNum < dataRowNodeList.getLength(); rowNum++) {
                Node dataRowNode = dataRowNodeList.item(rowNum);
                for (int colNum = 0; colNum < nbColumns; colNum++) {
                    Element fstElmnt = (Element) dataRowNode;
                    NodeList dateItemNodeList = fstElmnt.getElementsByTagName("DATA-ITEM");
                    Element fstNmElmnt = (Element) dateItemNodeList.item(colNum);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    rowSet[rowNum][colNum] = (((Node) fstNm.item(0)).getNodeValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowSet;
    }
}
