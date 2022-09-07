class BackupThread extends Thread {
    public static Document validateContentViaPost(String content) {
        String url = "http://validator.w3.org/check";
        try {
            String boundary = "AaB03x";
            String eol = "\r\n";
            HttpURLConnection httpC = (HttpURLConnection) new URL(url).openConnection();
            httpC.setDoOutput(true);
            httpC.setDoInput(true);
            httpC.setUseCaches(false);
            httpC.setRequestMethod("POST");
            httpC.setRequestProperty("Connection", "Keep-Alive");
            httpC.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            httpC.setRequestProperty("Content-Length", "xxx");
            DataOutputStream out = new DataOutputStream(httpC.getOutputStream());
            out.writeBytes("--");
            out.writeBytes(boundary);
            out.writeBytes(eol);
            out.writeBytes("Content-Disposition: form-data; name=\"output\"");
            out.writeBytes(eol);
            out.writeBytes("Content-Type: text/html; charset=UTF-8");
            out.writeBytes(eol);
            out.writeBytes(eol);
            out.writeBytes("xml");
            out.writeBytes(eol);
            out.writeBytes("--");
            out.writeBytes(boundary);
            out.writeBytes(eol);
            out.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"test.html\"");
            out.writeBytes(eol);
            out.writeBytes("Content-Type: text/html; charset=UTF-8");
            out.writeBytes(eol);
            out.writeBytes(eol);
            out.writeBytes(content + "\n");
            out.writeBytes(eol + "--" + boundary + "--" + eol);
            out.flush();
            out.close();
            Document document = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(httpC.getInputStream());
            Thread.sleep(1000);
            return document;
        } catch (Exception e) {
            logger.error("Error with POST validation", e);
            return null;
        }
    }
}
