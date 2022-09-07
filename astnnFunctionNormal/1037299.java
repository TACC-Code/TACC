class BackupThread extends Thread {
    private void updateProgress() {
        final double READ_WT = 0.33;
        final double WRITE_WT = 0.67;
        double readProgress = _segCount;
        if (_estByteLimit > 0 && _byteCount > 0) readProgress += (double) _byteCount / _estByteLimit;
        double writeProgress = _fileCount;
        double scaledProgress = READ_WT * readProgress / Math.max(_estSegLimit, 1) + WRITE_WT * writeProgress / Math.max(_estFileLimit, 1);
        int percent = (int) Math.round(100 * scaledProgress);
        if (percent > 100) percent = 100;
        if (percent > _prevPercent) {
            _prevPercent = percent;
            _props.setInteger(Pack200.Unpacker.PROGRESS, percent);
            if (_verbose > 0) Utils.log.info("progress = " + percent);
        }
    }
}
