class BackupThread extends Thread {
    public static void createUpgrade(List<JarFile> clientJarFiles, List<JarFile> serverJarFiles) throws Exception {
        FileSystemUtil.createDirectory(targetRootUpgradeDir);
        copyUpgradeDir("/01schema/");
        FileUtils.copyFile(new File(trunkDir + "/build/deploypos_schema.sql"), new File(targetRootUpgradeDir + "/01schema/schema_only.sql"));
        copyUpgradeDir("/02data/");
        File fromLibDir = new File(targetRootClientDir + "/lib");
        File toLibDir = new File(targetRootUpgradeDir + "/02data/client/lib");
        FileSystemUtil.createDirectory(targetRootUpgradeDir + "/02data/client");
        FileUtils.copyDirectory(fromLibDir, toLibDir);
        copyUpgradeDir("/03server/");
        FileUtils.copyFile(new File(targetServerDir + "/server/default/deploy/patientis.ejb3"), new File(targetRootUpgradeDir + "/03server/patientis.ejb3"));
        FileUtils.copyFile(new File(targetServerDir + "/server/default/deploy/patientos.war"), new File(targetRootUpgradeDir + "/03server/patientos.war"));
        for (JarFile jarfile : serverJarFiles) {
            if (Converter.isTrimmedSameIgnoreCase(jarfile.getVersion(), Software.getVersion())) {
                FileUtils.copyFile(jarfile.getSourceFile(), new File(targetRootUpgradeDir + "/03server/" + jarfile.getSourceFile().getName()));
            }
        }
        copyUpgradeDir("/04client/");
        FileUtils.copyFile(new File(targetRootClientDir + "/lib/patientis.jar"), new File(targetRootUpgradeDir + "/04client/patientis.jar"));
        FileUtils.copyFile(new File(targetRootClientDir + "/lib/resources.jar"), new File(targetRootUpgradeDir + "/04client/resources.jar"));
        for (JarFile jarfile : clientJarFiles) {
            if (Converter.isTrimmedSameIgnoreCase(jarfile.getVersion(), Software.getVersion())) {
                FileUtils.copyFile(jarfile.getSourceFile(), new File(targetRootUpgradeDir + "/04client/" + jarfile.getSourceFile().getName()));
            }
        }
    }
}
