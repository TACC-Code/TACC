class BackupThread extends Thread {
    public DecimatedSonaTrail(AudioTrail fullScale, int model) throws IOException {
        super();
        switch(model) {
            case MODEL_SONA:
                decimator = new SonaDecimator();
                break;
            default:
                throw new IllegalArgumentException("Model " + model);
        }
        this.fullScale = fullScale;
        this.model = model;
        constQ = new ConstQ();
        final Preferences cqPrefs = AbstractApplication.getApplication().getUserPrefs().node(PrefsUtil.NODE_VIEW).node(PrefsUtil.NODE_SONAGRAM);
        constQ.readPrefs(cqPrefs);
        constQ.setSampleRate(fullScale.getRate());
        System.out.println("Creating ConstQ Kernels...");
        constQ.createKernels();
        numKernels = constQ.getNumKernels();
        filterBuf = new float[numKernels];
        fftSize = constQ.getFFTSize();
        modelChannels = constQ.getNumKernels();
        fullChannels = fullScale.getChannelNum();
        decimChannels = fullChannels * modelChannels;
        System.out.println("...done.");
        stepSize = Math.max(64, Math.min(fftSize, MathUtil.nextPowerOfTwo((int) (constQ.getMaxTimeRes() / 1000 * fullScale.getRate() / Math.sqrt(2)))));
        int decimKorr, j;
        for (decimKorr = 1, j = stepSize; j > 2; decimKorr++, j >>= 1) ;
        final int decimations[] = { decimKorr };
        SUBNUM = decimations.length;
        this.decimHelps = new DecimationHelp[SUBNUM];
        for (int i = 0; i < SUBNUM; i++) {
            this.decimHelps[i] = new DecimationHelp(fullScale.getRate(), decimations[i]);
        }
        MAXSHIFT = decimations[0];
        MAXCOARSE = 1 << MAXSHIFT;
        MAXMASK = -MAXCOARSE;
        MAXCEILADD = MAXCOARSE - 1;
        if (log10 == null) {
            log10 = new FastLog(10, 11);
        }
        tmpBufSize = fftSize;
        tmpBufSize2 = 1 << decimations[0];
        for (int i = 1; i < SUBNUM; i++) {
            tmpBufSize2 = Math.max(tmpBufSize2, 1 << (decimations[i] - decimations[i - 1]));
        }
        setRate(fullScale.getRate());
        fullScale.addDependant(this);
        addAllDepAsync();
    }
}
