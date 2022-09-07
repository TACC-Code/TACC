class BackupThread extends Thread {
            public void run() {
                try {
                    System.out.println("Reading with cookies from u = " + u);
                    URLConnection urlConn = u.openConnection();
                    urlConn.setRequestProperty("Cookie", cookie);
                    urlConn.connect();
                    InputStream is = urlConn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bin = new BufferedReader(isr);
                    String line = null;
                    StringBuffer content = new StringBuffer();
                    while ((line = bin.readLine()) != null) content.append(line).append("\n");
                    bin.close();
                    if (content.length() > 0) gui.setProgram(content.toString());
                } catch (IOException e) {
                    if (DebugUtil.DEBUGGING) {
                        e.printStackTrace();
                    }
                }
            }
}
