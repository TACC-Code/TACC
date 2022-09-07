class BackupThread extends Thread {
    static void download(URL url, File target) throws IOException {
        System.out.print("Downloading " + url + "...");
        URLConnection con = url.openConnection();
        con.setConnectTimeout(Prefs.current.timeout);
        con.setReadTimeout(Prefs.current.timeout);
        InputStream is = con.getInputStream();
        OutputStream os = new FileOutputStream(target);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = is.read(buffer)) > 0) os.write(buffer, 0, bytesRead);
        os.close();
        is.close();
        System.out.println("OK.");
    }
}
