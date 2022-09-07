class BackupThread extends Thread {
    public String getTextFromURL(String url, String username, String password) throws Exception {
        this.url = url;
        this.username = username;
        this.password = password;
        String content = "";
        try {
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getUsername(), getPassword().toCharArray());
                }
            });
            URLConnection urlCon = (new URL(url)).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                content += line + "\n";
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
