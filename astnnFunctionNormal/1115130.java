class BackupThread extends Thread {
    public Vector getDefinitions(int i) {
        Vector v = null;
        v = new Vector();
        if (this.wlv == null) {
            this.wlv = this.search();
        }
        int n = 0;
        if (wlv != null) {
            n = this.wlv.size();
        }
        int j = 0;
        int k = 0;
        boolean found = false;
        if (n > 0 && i < n) {
            while (!found && j < n) {
                WebLink wl = wlv.weblinkAt(j);
                String sdesc = wl.getText();
                String surl = wl.getURLString();
                if (surl.startsWith("http://") && !surl.contains(".google.") && !(sdesc.compareTo("Cached") == 0) && !surl.endsWith("pdf")) {
                    found = true && (k == i);
                    HttpURLConnection.setFollowRedirects(false);
                    URL url = null;
                    try {
                        url = new URL(surl);
                    } catch (Exception e) {
                    }
                    URLConnection c = null;
                    try {
                        c = url.openConnection();
                    } catch (Exception e) {
                    }
                    InputStream istream = null;
                    try {
                        istream = c.getInputStream();
                    } catch (Exception e) {
                    }
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new InputStreamReader(istream));
                        String str = null;
                        while ((str = reader.readLine()) != null) {
                            String ans = this.process(this.what, str);
                            if (ans != null) {
                                v.add(ans);
                            }
                        }
                    } catch (IOException e) {
                    }
                    k = k + 1;
                }
                j = j + 1;
            }
        }
        return v;
    }
}
