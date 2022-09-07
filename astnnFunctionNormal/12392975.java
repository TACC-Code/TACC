class BackupThread extends Thread {
    private synchronized void printStat(MethodKeyAccumulator handler) {
        writer.setValue(FIELD_CLASS, handler.getMethodKey().getClassName());
        writer.setValue(FIELD_METHOD, handler.getMethodKey().getMethodName());
        writer.setValue(FIELD_SIGNATURE, handler.getMethodKey().getSignature());
        writer.setValue(FIELD_TOTAL_ENTERS, handler.getTotalEnters());
        writer.setValue(FIELD_TOTAL_EXITS, handler.getTotalExits());
        writer.setValue(FIELD_TOTAL_ERRORS, handler.getTotalErrors());
        writer.setValue(FIELD_TOTAL_DURATION, handler.getTotalDurationNanos());
        writer.setValue(FIELD_MIN_DURATION, handler.getMinDurationNanos());
        writer.setValue(FIELD_MAX_DURATION, handler.getMaxDurationNanos());
        writer.setValue(FIELD_STD_DEVIATION, handler.getStdDeviation());
        writer.setValue(FIELD_SUM_OF_SQUARES, handler.getSumOfSquares());
        writer.setValue(FIELD_MAX_CONCUR_THREADS, handler.getMaxConcurrentThreads());
        writer.write();
    }
}
