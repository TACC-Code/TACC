class BackupThread extends Thread {
    private static void createInterfaces() throws Exception {
        FileSystemUtil.createDirectory(targetInterfacesDir);
        File fromDir = new File(mirthCleanDir);
        File toDir = new File(targetInterfacesDir + "/bin/");
        FileUtils.copyDirectory(fromDir, toDir);
        FileUtils.copyDirectory(new File(sourceDeployChannels), new File(targetInterfacesDir + "channels/"));
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/adt_in/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/adt_out/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/lab_results_in/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/lab_orders_out/");
        FileSystemUtil.createDirectory(targetInterfacesDir + "activity/oru_xml_in/");
        FileUtils.copyFile(new File(sourceDeployment + "/channels/interfaces.bat"), new File(targetInterfacesDir + "/bin/interfaces.bat"));
        FileUtils.copyFile(new File(sourceDeployment + "/channels/interfaces.sh"), new File(targetInterfacesDir + "/bin/interfaces.sh"));
    }
}
