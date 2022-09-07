class BackupThread extends Thread {
    public static String getMapperParameters(NeissModelXML resXML, StringBuilder errors) {
        if (errors == null) {
            errors = new StringBuilder();
        }
        String[] theFile = null;
        NeissMapperXML mapXML = new NeissMapperXML();
        Map<String, String> resMap = resXML.getResultsData();
        if (resMap.size() == 0) {
            System.err.println("MapperPortlet.getMapperParameters() warning: there are no results " + "in the XML, this function shoudn't have been called.");
            return mapXML.toString();
        }
        for (String desc : resMap.keySet()) {
            String urlStr = resMap.get(desc);
            try {
                URL url = new URL(urlStr);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                theFile = MapperCopy.analyseFile(br, null);
                if (theFile == null) {
                    errors.append("Warning. Could not parse the input for " + url.toString() + "\n");
                } else {
                    String filename = theFile[0];
                    String theGeom = theFile[2];
                    String areaCode = theFile[3];
                    ArrayList<String> mappableColumns = new ArrayList<String>();
                    for (int i = 4; i < theFile.length; i++) {
                        mappableColumns.add(theFile[i]);
                    }
                    mapXML.addDataFile(urlStr, filename, theGeom, areaCode, mappableColumns);
                }
            } catch (MalformedURLException ex) {
                errors.append("MalformedURLException trying to create a URL object from the " + "results url '" + urlStr + "' (description: '" + desc + "')");
                return null;
            } catch (IOException ex) {
                errors.append("IOException (" + ex.toString() + ") trying to create a URL object from the " + "results url '" + urlStr + "' (description: '" + desc + "'). ");
                return null;
            }
        }
        return mapXML.toString();
    }
}
