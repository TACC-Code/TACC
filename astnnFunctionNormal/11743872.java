class BackupThread extends Thread {
    public void execute() throws BuildException {
        if (splash != null) {
            splash.setVisible(false);
            getProject().removeBuildListener(splash);
            splash.dispose();
            splash = null;
        }
        log("Creating new SplashScreen", Project.MSG_VERBOSE);
        InputStream in = null;
        if (imgurl != null) {
            try {
                URLConnection conn = null;
                if (useProxy && (proxy != null && proxy.length() > 0) && (port != null && port.length() > 0)) {
                    log("Using proxied Connection", Project.MSG_DEBUG);
                    System.getProperties().put("http.proxySet", "true");
                    System.getProperties().put("http.proxyHost", proxy);
                    System.getProperties().put("http.proxyPort", port);
                    URL url = new URL(imgurl);
                    conn = url.openConnection();
                    if (user != null && user.length() > 0) {
                        String encodedcreds = new Base64Converter().encode(user + ":" + password);
                        conn.setRequestProperty("Proxy-Authorization", encodedcreds);
                    }
                } else {
                    System.getProperties().put("http.proxySet", "false");
                    System.getProperties().put("http.proxyHost", "");
                    System.getProperties().put("http.proxyPort", "");
                    log("Using Direction HTTP Connection", Project.MSG_DEBUG);
                    URL url = new URL(imgurl);
                    conn = url.openConnection();
                }
                conn.setDoInput(true);
                conn.setDoOutput(false);
                in = conn.getInputStream();
            } catch (Throwable ioe) {
                log("Unable to download image, trying default Ant Logo", Project.MSG_DEBUG);
                log("(Exception was \"" + ioe.getMessage() + "\"", Project.MSG_DEBUG);
            }
        }
        if (in == null) {
            ClassLoader cl = SplashTask.class.getClassLoader();
            if (cl != null) {
                in = cl.getResourceAsStream("images/ant_logo_large.gif");
            } else {
                in = ClassLoader.getSystemResourceAsStream("images/ant_logo_large.gif");
            }
        }
        boolean success = false;
        if (in != null) {
            DataInputStream din = new DataInputStream(in);
            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                int data;
                while ((data = din.read()) != -1) {
                    bout.write((byte) data);
                }
                log("Got ByteArray, creating splash", Project.MSG_DEBUG);
                try {
                    ImageIcon img = new ImageIcon(bout.toByteArray());
                    splash = new SplashScreen(img, progressRegExp, displayText);
                    success = true;
                } catch (Throwable e) {
                    logHeadless(e);
                }
            } catch (Exception e) {
                throw new BuildException(e);
            } finally {
                try {
                    din.close();
                } catch (IOException ioe) {
                    if (success) {
                        throw new BuildException(ioe);
                    }
                }
            }
        } else {
            try {
                splash = new SplashScreen("Image Unavailable.", progressRegExp, displayText);
                success = true;
            } catch (Throwable e) {
                logHeadless(e);
            }
        }
        if (success) {
            splash.setVisible(true);
            splash.toFront();
            getProject().addBuildListener(splash);
            try {
                Thread.sleep(showDuration);
            } catch (InterruptedException e) {
            }
        }
    }
}
