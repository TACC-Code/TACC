class BackupThread extends Thread {
    public void saveConfig() {
        File file = null;
        try {
            String home = System.getProperty("user.home");
            file = new File(home, EncogWorkBench.CONFIG_FILENAME);
            OutputStream os = new FileOutputStream(file);
            EncogWriteHelper out = new EncogWriteHelper(os);
            out.addSection("ENCOG");
            out.addSubSection("TRAINING");
            out.writeProperty(EncogWorkBenchConfig.PROPERTY_DEFAULT_ERROR, this.defaultError);
            out.writeProperty(EncogWorkBenchConfig.PROPERTY_THREAD_COUNT, this.threadCount);
            out.writeProperty(EncogWorkBenchConfig.PROPERTY_USE_GPU, this.useOpenCL);
            out.writeProperty(EncogWorkBenchConfig.PROPERTY_ERROR_CALC, this.errorCalculation);
            out.flush();
            os.close();
        } catch (IOException ex) {
            if (file != null) EncogWorkBench.displayError("Can't write config file", file.toString());
        }
    }
}
