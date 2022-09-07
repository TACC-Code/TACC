class BackupThread extends Thread {
    public final void operate(AChannelSelection chs) {
        MMArray s = chs.getChannel().getSamples();
        int o = chs.getOffset();
        int l = chs.getLength();
        float oldRms = 0;
        if (rmsAdaptionEnabled) {
            oldRms = AOToolkit.rmsAverage(s, o, l);
        }
        chs.getChannel().markChange();
        if (re == null) {
            re = new MMArray(fftBlockLength, 0);
        }
        if (re.getLength() != fftBlockLength) {
            re.setLength(fftBlockLength);
        }
        if (im == null) {
            im = new MMArray(fftBlockLength, 0);
        }
        if (im.getLength() != fftBlockLength) {
            im.setLength(fftBlockLength);
        }
        if (tmp == null) {
            tmp = new MMArray(fftBlockLength, 0);
        }
        if (tmp.getLength() != l) {
            tmp.setLength(l);
        }
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            int bufferOperations = (int) (l / fftBlockLength / (1f - overlapFactor) + 1);
            for (int i = -1; i < bufferOperations + 1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / bufferOperations)) return;
                int ii = (int) (o + i * fftBlockLength * (1f - overlapFactor));
                for (int j = 0; j < fftBlockLength; j++) {
                    int jj = ii + j;
                    if ((jj >= 0) && (jj < o + l)) {
                        re.set(j, s.get(jj));
                    } else {
                        re.set(j, 0);
                    }
                    im.set(j, 0);
                }
                AOToolkit.applyFFTWindow(fftWindowType, re, fftBlockLength);
                AOToolkit.complexFft(re, im);
                processor.process(re, im, ii, fftBlockLength / 2);
                AOToolkit.complexIfft(re, im);
                for (int j = 0; j < fftBlockLength; j++) {
                    int jjj = ii - o + j;
                    if ((jjj >= 0) && (jjj < l)) {
                        tmp.set(jjj, tmp.get(jjj) + re.get(j));
                    }
                }
                if (zeroCrossEnabled) {
                    AOToolkit.applyZeroCross(tmp, ii - o);
                }
            }
            for (int i = 0; i < l; i++) {
                s.set(o + i, chs.mixIntensity(o + i, s.get(o + i), tmp.get(i)));
            }
            if (rmsAdaptionEnabled) {
                float newRms = AOToolkit.rmsAverage(s, o, l);
                AOToolkit.multiply(s, o, l, (float) (oldRms / newRms));
            }
            if (zeroCrossEnabled) {
                AOToolkit.applyZeroCross(s, o);
                AOToolkit.applyZeroCross(s, o + l);
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (Exception e) {
            Debug.printStackTrace(5, e);
        }
    }
}
