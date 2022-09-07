class BackupThread extends Thread {
    public static double compute(final Evaluator F, final double a, final double b, final int nstart, final double eps, final int maxiter) throws ConstructionException {
        final double t[] = new double[maxiter];
        int n = nstart;
        double h = (b - a) / n;
        double tlast = t[0] = (F.evaluateF(a) + F.evaluateF(b) + 2 * sumUp(F, a + h, h, n - 2)) * h / 2;
        double old = t[0];
        for (int i = 1; i < maxiter; i++) {
            h = h / 2;
            n = n * 2;
            t[i] = tlast / 2 + sumUp(F, a + h, 2 * h, n / 2 - 1) * h;
            tlast = t[i];
            double q = 4;
            for (int j = i - 1; j >= 0; j--) {
                t[j] = t[j + 1] + (t[j + 1] - t[j]) / (q - 1);
                q = q * 4;
            }
            final double res = t[0];
            if (Math.abs((res - old) / res) < eps) return res;
            old = res;
        }
        return tlast;
    }
}
