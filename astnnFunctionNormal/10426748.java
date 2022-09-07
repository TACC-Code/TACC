class BackupThread extends Thread {
    public void load(User user) throws IOException, JDOMException {
        URL url = new URL(FetionConfig.getString("server.nav-system-uri"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("User-Agent", "IIC2.0/PC " + FetionClient.PROTOCOL_VERSION);
        String accountType = null;
        if (user.getMobile() > 0) {
            accountType = "mobile-no=\"" + user.getMobile() + "\"";
        } else if (user.getFetionId() > 0) {
            accountType = "sid=\"" + user.getFetionId() + "\"";
        } else if (user.getEmail() != null) {
            accountType = "email=\"" + user.getEmail() + "\"";
        } else {
            throw new IllegalStateException("Invalid CMCC mobile number, FetionId or Registered Email.");
        }
        String content = "<config><user " + accountType + " /><client type=\"PC\" version=\"" + FetionClient.PROTOCOL_VERSION + "\" platform=\"W5.1\" /><servers version=\"0\" /><service-no version=\"0\" /><parameters version=\"0\" /><hints version=\"0\" /><http-applications version=\"0\" /><client-config version=\"0\" /><services version=\"0\" /></config>";
        OutputStream out = conn.getOutputStream();
        out.write(content.getBytes());
        out.flush();
        SAXBuilder builder = new SAXBuilder();
        this.document = builder.build(conn.getInputStream());
        this.isLoaded = true;
    }
}
