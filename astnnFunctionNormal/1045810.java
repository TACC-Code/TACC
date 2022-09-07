class BackupThread extends Thread {
    private static boolean downloadChangelogFile(PackageManager pm, Package pkg, String changelogDirectory, String changeDir) throws PackageManagerException {
        boolean ret = false;
        try {
            final File changelogDir = new File((new StringBuilder()).append(changelogDirectory).append(changeDir).toString());
            String serverPath = pkg.getServerPath();
            serverPath = serverPath.substring(0, serverPath.lastIndexOf("/") + 1);
            final BufferedInputStream in = new BufferedInputStream(new ConnectToServer(pm).getInputStream((new StringBuilder()).append(serverPath).append(pkg.getName()).append(".changelog").toString()));
            if (!changelogDir.isDirectory()) changelogDir.mkdirs();
            final File rename = new File(changelogDir.getCanonicalPath(), (new StringBuilder()).append(pkg.getName()).append(".changelog").toString());
            final FileOutputStream out = new FileOutputStream(rename);
            final byte buf[] = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            in.close();
            ret = true;
        } catch (final Exception e) {
            if (null != pm) {
                logger.warn(e);
                pm.addWarning(e.toString());
            } else logger.warn(e);
        }
        return ret;
    }
}
