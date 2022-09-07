class BackupThread extends Thread {
    public static JarFile getLaunchJar() {
        if (launchJar != null) return launchJar;
        if (launchJarPath == null) return null;
        boolean isWebFile = launchJarPath.startsWith("http:");
        try {
            if ((OSPRuntime.applet == null) && !isWebFile) {
                launchJar = new JarFile(launchJarPath);
            } else {
                URL url;
                if (isWebFile) {
                    url = new URL("jar:" + launchJarPath + "!/");
                } else {
                    url = new URL("jar:file:/" + launchJarPath + "!/");
                }
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                launchJar = conn.getJarFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return launchJar;
    }
}
