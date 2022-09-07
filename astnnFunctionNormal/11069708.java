class BackupThread extends Thread {
    private List<JarFile> createClient() throws Exception {
        FileSystemUtil.createDirectory(targetRootClientDir);
        FileSystemUtil.createDirectory(targetClientBinDir);
        FileUtils.copyFile(new File(sourceDeployConfigure + "/checkinstall.bat"), new File(targetClientBinDir + "/checkinstall.bat"));
        FileUtils.copyFile(new File(sourceDeployConfigure + "/checkinstall.sh"), new File(targetClientBinDir + "/checkinstall.bat"));
        List<JarFile> clientJars = JarFile.createTargetJars(sourceJarList, trunkDir, targetRootClientDir, true, false);
        Client client = Client.load(sourceClient);
        client.writeRunBatchFile(targetClientBinWindows, clientJars, ";", "../", true, javaVersion, true);
        client.writeRunBatchFile(targetClientBinUNIX, clientJars, ":", "../", true, javaVersion, false);
        client.writeJarsigner(jarSignFile, clientJars);
        FileUtils.copyFile(new File(splashFile), new File(targetClientBinDir + "splash.gif"));
        for (File file : FileSystemUtil.getFiles(sourceClientDLLS)) {
            if (!file.isDirectory()) {
                FileUtils.copyFile(file, new File(targetClientBinDir + "/" + file.getName()));
            }
        }
        StringBuffer jnlp = new StringBuffer();
        for (JarFile jar : clientJars) {
            jnlp.append("    <jar href=\"client/lib/");
            jnlp.append(jar.getLibraryFile());
            jnlp.append("\"/>\r\n");
        }
        FileSystemUtil.createDirectory(targetServerDir);
        FileSystemUtil.createFile(targetServerDir + "jnlp-resources.txt", jnlp.toString());
        return clientJars;
    }
}
