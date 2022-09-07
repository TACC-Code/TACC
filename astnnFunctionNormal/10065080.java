class BackupThread extends Thread {
    public String process(Archive a) {
        SessionManager sessionManager = SessionManager.getInstance();
        this.a = a;
        init();
        constructBody();
        close();
        String path = sessionManager.getSessionName() + ".smil";
        File f = new File(sessionManager.getSessionDir(), path);
        if (f.exists()) {
            f.renameTo(new File(f.toString() + ".bak"));
            f = new File(sessionManager.getSessionDir(), path);
        }
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
            StringReader in = new StringReader(sb.toString());
            int b;
            while ((b = in.read()) != -1) out.write(b);
            out.flush();
            out.close();
            in.close();
            a.add(f, path);
            return path;
        } catch (IOException ioe) {
            m_logCat.error("An error occured while saving " + path, ioe);
        }
        return null;
    }
}
