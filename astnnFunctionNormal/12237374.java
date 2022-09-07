class BackupThread extends Thread {
    private void doTest(File srcDir) throws Exception {
        System.out.println("\nNow testing " + srcDir.getName());
        File tmpDir = new File(getTmpDir(), "validatorTest");
        File configDir = new File(tmpDir, srcDir.getName());
        configDir.mkdirs();
        FileUtils.copyFiles(srcDir, configDir);
        File config = new File(configDir, "config.txt");
        new FileOutputStream(config).close();
        int errors = Validator.validate(config.getAbsolutePath());
        assertEquals(1, errors);
    }
}
