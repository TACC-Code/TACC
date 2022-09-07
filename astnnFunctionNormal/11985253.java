class BackupThread extends Thread {
    public Class loadClass(String _name, boolean _resolve) throws ClassNotFoundException {
        Class r = get(_name);
        if (r == null) {
            if (!_name.startsWith("java")) {
                try {
                    String n = _name.replace('.', '/') + ".class";
                    InputStream in = null;
                    String classpath = System.getProperty("java.class.path");
                    StringTokenizer st = new StringTokenizer(classpath, ":");
                    while (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        if (in == null) {
                            try {
                                File d = new File(t);
                                if (d.isDirectory()) {
                                    File f = new File(d, n);
                                    if (f.exists()) {
                                        in = new FileInputStream(f);
                                    }
                                }
                            } catch (Exception ex) {
                            }
                        }
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    if (in != null) while (in.available() > 0) out.write(in.read());
                    byte[] data = out.toByteArray();
                    r = defineClass(_name, data, 0, data.length);
                    if (only_ == null) only_ = r;
                    putLocal(r);
                    if (_resolve) resolveClass(r);
                } catch (Throwable th) {
                    r = null;
                }
            }
        }
        if (r == null) {
            r = Class.forName(_name);
            putGlobal(r);
        }
        return r;
    }
}
