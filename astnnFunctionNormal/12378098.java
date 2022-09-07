class BackupThread extends Thread {
    public static String webService(String siteUrl, String login, String password, String station, String element, String day) throws Exception {
        Service service = new Service();
        Call call = (Call) service.createCall();
        if (login != null) {
            call.setUsername(login);
            if (password != null) {
                call.setPassword(password);
            }
            System.err.println("Info: authentication user=" + login + " passwd=" + password + " at " + siteUrl);
        }
        call.setTargetEndpointAddress(new URL(siteUrl));
        call.setOperationName("getData");
        String url = (String) call.invoke(new Object[] { station, element, day });
        String fileName = null;
        if (url == null) {
            throw new Exception("Error: result URL is null");
        } else {
            System.err.println("Info: result URL is " + url);
            URL dataurl = new URL(url);
            String filePath = dataurl.getFile();
            if (filePath == null) {
                throw new Exception("Error: data file name is null");
            } else {
                fileName = filePath.substring(filePath.lastIndexOf("/") < 0 ? 0 : filePath.lastIndexOf("/") + 1);
                System.err.println("Info: local file name is " + fileName);
            }
            FileOutputStream file = new FileOutputStream(fileName);
            if (file == null) {
                throw new Exception("Error: file output stream is null");
            }
            InputStream strm = dataurl.openStream();
            if (strm == null) {
                throw new Exception("Error: data input stream is null");
            } else {
                int c;
                while ((c = strm.read()) != -1) {
                    file.write(c);
                }
            }
        }
        return fileName;
    }
}
