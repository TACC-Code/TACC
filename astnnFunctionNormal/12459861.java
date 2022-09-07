class BackupThread extends Thread {
    public static void benchmarkForward_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DCT 1D of size 2^" + exponent);
            FloatDct1D dct = new FloatDct1D(N);
            x = new float[N];
            if (doWarmup) {
                IoUtils.fillMatrix_1D(N, x);
                dct.forward(x, doScaling);
                IoUtils.fillMatrix_1D(N, x);
                dct.forward(x, doScaling);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_1D(N, x);
                elapsedTime = System.nanoTime();
                dct.forward(x, doScaling);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dct = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDCT_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
}
