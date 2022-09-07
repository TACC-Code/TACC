class BackupThread extends Thread {
    public void calcMassAlignmentError() throws JPLConsensusBuilderException {
        int totNrPeaks = 0;
        for (JPLExpPeakList spectrum : cluster) {
            totNrPeaks += spectrum.getNbPeak();
        }
        double[] masses = new double[totNrPeaks];
        double[] mzDiffSingle = new double[totNrPeaks];
        double[] mzDiffComb = new double[totNrPeaks];
        int pos = 0;
        int j = 0;
        for (JPLExpPeakList spectrum : cluster) {
            double[] mz = spectrum.getMzs();
            System.arraycopy(mz, 0, masses, pos, mz.length);
            pos += spectrum.getNbPeak();
            for (int i = 1; i < mz.length; i++) {
                mzDiffSingle[j] = mz[i] - mz[i - 1];
                j++;
            }
        }
        for (; j < totNrPeaks; j++) mzDiffSingle[j] = 10000.0;
        Arrays.sort(mzDiffSingle);
        Arrays.sort(masses);
        double lambda = 0;
        int cnt = 0;
        for (int i = 0; i < masses.length - 1; i++) {
            mzDiffComb[i] = masses[i + 1] - masses[i];
            if (mzDiffComb[i] < mzAlignError) {
                lambda += mzDiffComb[i];
                cnt++;
            }
        }
        lambda = lambda / cnt;
        if (cnt < 10) {
            throw new JPLConsensusBuilderException("Not enough matching peaks between spectra!");
        }
        double th = -lambda * Math.log(0.01);
        cnt = 0;
        for (int i = 0; mzDiffSingle[i] < th; i++) cnt++;
        if (cnt / cluster.size() > 5) {
            throw new JPLConsensusBuilderException("Peaks within same spectrum are too close!");
        }
        mzAlignError = th;
    }
}
