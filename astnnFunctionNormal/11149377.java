class BackupThread extends Thread {
    private GradientModel(ProfileColorSpace pcs, LCDTarget target, CIEXYZ white) {
        this.pcs = pcs;
        if (white == null) {
            this.white = (CIEXYZ) pcs.getReferenceWhite().clone();
        } else {
            this.white = (CIEXYZ) white.clone();
        }
        if (target != null) {
            this.target = target;
            this.imageChannel = target.getTargetChannel();
        }
        initCAM();
    }
}
