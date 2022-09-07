class BackupThread extends Thread {
    public Vector getDefinitions() {
        Vector v = null;
        v = new Vector();
        if (this.wlv == null) {
            this.wlv = this.search();
        }
        if (wlv != null) {
            int n = wlv.size();
            for (int i = 0; i < n; i++) {
                WebLink wl = wlv.weblinkAt(i);
                String sdesc = wl.getText();
                String surl = wl.getURLString();
                if (surl.startsWith("http://") && !surl.contains(".google.") && !(sdesc.compareTo("Cached") == 0) && !surl.endsWith("pdf")) {
                    HttpURLConnection.setFollowRedirects(false);
                    URL url = null;
                    try {
                        url = new URL(surl);
                    } catch (Exception e) {
                        break;
                    }
                    URLConnection c = null;
                    try {
                        c = url.openConnection();
                    } catch (Exception e) {
                        break;
                    }
                    InputStream istream = null;
                    try {
                        istream = c.getInputStream();
                    } catch (Exception e) {
                        break;
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
                        break;
                    }
                }
            }
        }
        return v;
    }
}
