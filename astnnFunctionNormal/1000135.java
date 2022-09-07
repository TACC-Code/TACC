class BackupThread extends Thread {
    public final void operate(AChannel2DSelection chs) {
        Debug.println(6, "block operate...");
        if (chs.getArea().isSomethingSelected()) {
            MMArray s = chs.getChannel().getSamples();
            int o = Math.min((int) chs.getArea().getXMin(), s.getLength() - 1);
            int l;
            if (chs.getArea().getXMax() < chs.getArea().getXMin()) {
                l = 0;
            } else {
                l = Math.max((int) chs.getArea().getXMax() - o, 1);
            }
            Debug.println(6, "block operate o=" + o + " l=" + l);
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
                tmp = new MMArray(l, 0);
            }
            if (tmp.getLength() != l) {
                tmp.setLength(l);
            }
            try {
                LProgressViewer.getInstance().entrySubProgress(0.7);
                float blockIncrement = 1f - overlapFactor;
                int bufferOperations = (int) ((l / fftBlockLength / blockIncrement) + 1);
                for (int i = -1; i < bufferOperations + 1; i++) {
                    if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / bufferOperations)) return;
                    int ii = (int) (o + i * fftBlockLength * blockIncrement);
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
                    boolean changed = processor.process(re, im, ii, fftBlockLength / 2, chs);
                    AOToolkit.complexIfft(re, im);
                    for (int j = 0; j < fftBlockLength; j++) {
                        int jj = ii - o + j;
                        if ((jj >= 0) && (jj < tmp.getLength())) {
                            tmp.set(jj, tmp.get(jj) + re.get(j));
                        }
                    }
                    if (zeroCrossEnabled) {
                        AOToolkit.applyZeroCross(tmp, ii - o);
                    }
                }
                for (int i = 0; i < l; i++) {
                    s.set(o + i, tmp.get(i));
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
}
