class BackupThread extends Thread {
    private List<JarFile> createAppServer() throws Exception {
        FileSystemUtil.createDirectory(targetServerDir);
        copyServerDir("bin");
        for (int i = 1; i < 10; i++) {
            File toDir = new File(targetServerDir + sep + "bin/../../data/" + i);
            File fromDir = new File("C:\\dev/local/devpos/data/" + i);
            if (fromDir.exists()) {
                FileUtils.copyDirectory(fromDir, toDir);
                ;
            } else if (i == 1) {
                System.err.println("not found " + fromDir);
                System.exit(1);
            }
        }
        copyServerDir("lib");
        copyServerDir("server/default");
        FileSystemUtil.createDirectory(targetServerDir + "/server/default/log");
        List<JarFile> jarFiles = JarFile.createTargetJars(sourceJarList, trunkDir, targetServerDir, false, true);
        JBoss.updatePorts(targetServerDir, 7106);
        JBoss.doNotUseHostName(targetServerDir);
        JBoss.updateJbossStandard(targetServerDir, serverSetupDir, sourceDeployment);
        writeAppServer(targetServerDir + "bin/appserver.bat", true, "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql://localhost:5432/demopos", "demopos", "demopos");
        writeAppServer(targetServerDir + "bin/appserver.sh", false, "org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql://localhost:5432/demopos", "demopos", "demopos");
        FileUtils.copyFile(new File(sourceDeployConfigure + "/pos.server.properties"), new File(targetServerDir + "bin/pos.server.properties"));
        FileUtils.copyFile(new File(sourceDeployConfigure + "/pos.keystore"), new File(targetServerDir + "bin/pos.keystore"));
        return jarFiles;
    }
}
