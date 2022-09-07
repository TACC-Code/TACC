class BackupThread extends Thread {
    public void testQMAdhocQueryRequest(String xmlFileName) throws FileNotFoundException, IOException, MalformedURLException {
        String xmlAdhocQueryRequest = "";
        String input;
        File xmlInputFile = new File(xmlFileName);
        BufferedReader in = new BufferedReader(new FileReader(xmlInputFile));
        while ((input = in.readLine()) != null) {
            xmlAdhocQueryRequest = xmlAdhocQueryRequest + input;
        }
        in.close();
        URL url = new URL(restURL);
        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoOutput(true);
        PrintWriter out = new PrintWriter(httpConnection.getOutputStream());
        out.println("xmldoc=" + URLEncoder.encode(xmlAdhocQueryRequest, "UTF-8"));
        out.close();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        String inputLine;
        while ((inputLine = in2.readLine()) != null) System.out.println(inputLine);
        in2.close();
    }
}
