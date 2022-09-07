class BackupThread extends Thread {
    private void doComparison(Stats javaStats, Stats frameworkStats) {
        double readGain = (javaStats.avg("read-java") - frameworkStats.avg("read-framework")) / javaStats.avg("read-java");
        readGain = ((long) (readGain * 10000)) / 100d;
        double writeGain = (javaStats.avg("write-java") - frameworkStats.avg("write-framework")) / javaStats.avg("write-java");
        writeGain = ((long) (writeGain * 10000)) / 100d;
        double byteSaving = (double) (javaStats.length - frameworkStats.length) / javaStats.length;
        byteSaving = ((long) (byteSaving * 10000)) / 100d;
        LOG.info("RecordJUnitTest.doComparison: readGain=" + readGain + "%" + " writeGain=" + writeGain + "%" + " byteSaving=" + byteSaving + "%");
    }
}
