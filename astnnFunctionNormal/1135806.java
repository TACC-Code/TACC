class BackupThread extends Thread {
    private void calculateBasis() {
        if (updateBasis) {
            Double[] x = knotLocations.getValues();
            Double[] y = knotValues.getValues();
            rangeMin = x[0];
            rangeMax = x[x.length - 1];
            for (int i = 0; i < n - 1; i++) {
                h[i] = x[i + 1] - x[i];
                deltaY[i] = y[i + 1] - y[i];
            }
            hMatrix.set(0, 0, 1.0);
            yByH.set(0, 0.0);
            for (int i = 1; i < n - 2; i++) {
                hMatrix.set(i, i - 1, h[i - 1]);
                hMatrix.set(i, i, 2 * (h[i] + h[i - 1]));
                hMatrix.set(i, i + 1, h[i]);
                yByH.set(i, 6 * (deltaY[i] / h[i] - deltaY[i - 1] / h[i - 1]));
            }
            hMatrix.set(n - 1, n - 1, 1.0);
            yByH.set(n - 1, 0.0);
            hMatrix.solve(yByH, z);
            updateBasis = false;
        }
    }
}
