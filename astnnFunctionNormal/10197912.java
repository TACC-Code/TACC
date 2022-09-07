class BackupThread extends Thread {
    private void assembleLibraries(ExtensionPoint exnPt, File localDestDir) throws IOException {
        for (Extension exn : exnPt.getConnectedExtensions()) {
            List<File> srcs = new ArrayList<File>();
            Parameter jarParam = exn.getParameter("jar");
            if (jarParam != null) {
                String filename = jarParam.valueAsString();
                File src = getFilePath(exn.getDeclaringPluginDescriptor(), filename);
                srcs.add(src);
            }
            Parameter dirParam = exn.getParameter("dir");
            if (dirParam != null) {
                String dirname = dirParam.valueAsString();
                File srcDir = getFilePath(exn.getDeclaringPluginDescriptor(), dirname);
                for (Object obj : FileUtils.listFiles(srcDir, new String[] { "jar" }, false)) {
                    File src = (File) obj;
                    srcs.add(src);
                }
            }
            for (File src : srcs) {
                File dest = new File(localDestDir, "/lib/" + src.getName());
                dest.getParentFile().mkdirs();
                logger.debug("Copy " + src.getPath() + " to " + dest.getPath());
                FileUtils.copyFile(src, dest);
            }
        }
    }
}
