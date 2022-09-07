class BackupThread extends Thread {
    private ArrayList<String> getVersions() {
        ArrayList<String> versions = new ArrayList<String>();
        try {
            URL xmlUrl = new URL(xmlPath);
            URLConnection urlConnection = xmlUrl.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();
            SAXBuilder sxb = new SAXBuilder();
            try {
                xmlDocument = sxb.build(stream);
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element racine = xmlDocument.getRootElement();
            List listVersions = racine.getChildren("version");
            Iterator iteratorVersions = listVersions.iterator();
            while (iteratorVersions.hasNext()) {
                Element version = (Element) iteratorVersions.next();
                Element elementNom = version.getChild("nom");
                versions.add(elementNom.getText());
            }
            Collections.sort(versions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return versions;
    }
}
