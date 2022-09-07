class BackupThread extends Thread {
    private final boolean ignoreChannel(RGBBase.Channel ch) {
        RGBBase.Channel targetch = target != null ? target.getTargetChannel() : null;
        return target != null && (targetch == null ? false : ch != targetch);
    }
}
