class BackupThread extends Thread {
    public static <S> ServiceLoader<S> load(Class<S> service, ClassLoader loader) {
        try {
            Enumeration<URL> resources = loader.getResources(PREFIX + service.getCanonicalName());
            final ArrayList<String> cnames = new ArrayList<String>();
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    line = line.trim();
                    final int startCommentIndex = line.indexOf(0x23) >= 0 ? line.indexOf(0x23) : line.length();
                    line = line.substring(0, startCommentIndex).trim();
                    if (line.length() > 0) {
                        cnames.add(line);
                    }
                }
            }
            return new ServiceLoader<S>(service.getCanonicalName(), loader, cnames);
        } catch (Exception transform) {
            throw new RuntimeException(transform);
        }
    }
}
