class BackupThread extends Thread {
    public static byte[] getContent(String uri) throws HttpException, IOException {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(uri);
        method.setRequestHeader("User-Agent", USER_AGENTS[(int) (Math.random() * USER_AGENTS.length)]);
        client.executeMethod(method);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = method.getResponseBodyAsStream();
        int read = 0;
        byte[] buffer = new byte[4096];
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        method.releaseConnection();
        return out.toByteArray();
    }
}
