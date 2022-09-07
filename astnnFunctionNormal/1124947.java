class BackupThread extends Thread {
    public static KMLFile parseURL(URL urlName) throws Exception {
        InputStream is = null;
        KMLFile kml = null;
        try {
            URLConnection urlConn = urlName.openConnection();
            String contentType = urlConn.getHeaderField("Content-Type");
            System.out.println("ContentType: " + contentType);
            is = urlConn.getInputStream();
            if (urlName.getFile().endsWith("kml") || "application/vnd.google-earth.kml+xml".equals(contentType)) {
            } else if (urlName.getFile().endsWith("kmz") || "application/vnd.google-earth.kmz".equals(contentType)) {
                ZipInputStream zis = new ZipInputStream(is);
                ZipEntry entry = zis.getNextEntry();
                while (entry != null && !entry.getName().endsWith("kml")) {
                    entry = zis.getNextEntry();
                }
                if (entry == null) {
                    throw new Exception("No KML file found in the KMZ package");
                }
                is = zis;
            } else {
                throw new IOException("Not a KML/KMZ file.  Expected '.kml/.kmz' got '" + urlName + "'");
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element rootEl = doc.getDocumentElement();
            kml = new KMLFile();
            if (!"kml".equals(rootEl.getLocalName())) {
                throw new Exception("Not a KML file.  Expected 'kml' got '" + rootEl.getLocalName() + "'");
            }
            for (Element child = firstChildElement(rootEl); child != null; child = nextSiblingElement(child)) {
                String nodeName = child.getLocalName();
                if ("Document".equals(nodeName)) {
                    parseDocument(child, kml);
                } else if (FOLDER_NODE.equals(nodeName)) {
                    parseFolder(child, kml, kml.getRootFolder(), urlName);
                } else if (PLACEMARK_NODE.equals(nodeName)) {
                    parsePlacemark(child, kml, kml.getRootFolder(), urlName);
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (is != null) is.close();
        }
        return kml;
    }
}
