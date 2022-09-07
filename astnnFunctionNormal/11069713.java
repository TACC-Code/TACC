class BackupThread extends Thread {
    private void copyDemoDb() throws Exception {
        FileUtils.copyFile(new File(sourceDemoDb + "/demo.properties"), new File(targetDemoDb + "/demo.properties"));
        FileUtils.copyFile(new File(sourceDemoDb + "/demo.script"), new File(targetDemoDb + "/demo.script"));
    }
}
