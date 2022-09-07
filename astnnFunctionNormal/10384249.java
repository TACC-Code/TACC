class BackupThread extends Thread {
    private void doSave() throws Exception {
        this.log("--rlog save--");
        PrintWriter printout;
        URLConnection urlConn;
        URL uurl = new URL(logUrl);
        urlConn = uurl.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "text/plain");
        printout = new PrintWriter(urlConn.getOutputStream());
        if (saved) {
            buffy.insert(0, "#Previously saved!\n");
        }
        printout.write(buffy.toString());
        DataInputStream input;
        printout.flush();
        printout.close();
        input = new DataInputStream(urlConn.getInputStream());
        BufferedReader i = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String str;
        while (null != ((str = i.readLine()))) {
            emit(str);
        }
        input.close();
        saved = true;
    }
}
