class BackupThread extends Thread {
    private Element apiCall(String methodName, Map partialQuery) throws ConnectionException {
        Map query = null;
        if (partialQuery == null) {
            query = new HashMap();
        } else {
            query = new HashMap(partialQuery);
        }
        query.put("format", "xml");
        query.put("apiKey", apiKey);
        StringBuffer sb = new StringBuffer();
        for (Iterator it = query.entrySet().iterator(); it.hasNext(); ) {
            if (sb.length() > 0) sb.append('&');
            try {
                Map.Entry e = (Map.Entry) it.next();
                sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unexpected encoding error", e);
            }
        }
        String data = sb.toString();
        try {
            URL url = new URL(baseUrl + "/api/v2/" + methodName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            osw.write(data);
            osw.close();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(conn.getInputStream());
            Element response = (Element) doc.getFirstChild();
            if (!response.getAttribute("stat").equals("ok")) {
                throw new RuntimeException("Unexpected API error");
            }
            return response;
        } catch (MalformedURLException e) {
            throw new ConnectionException("Unexpected URL error", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Unexpected IO error", e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Unexpected XML error", e);
        } catch (SAXException e) {
            throw new RuntimeException("Unexpected XML error", e);
        }
    }
}
