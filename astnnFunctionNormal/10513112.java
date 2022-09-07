class BackupThread extends Thread {
    public DoubleSquareMatrix[] singularValueDecompose() {
        int i, j, k;
        final int N = numRows;
        final int Nm1 = N - 1;
        final double array[][] = new double[N][N];
        final double arrayU[][] = new double[N][N];
        final double arrayS[] = new double[N];
        final double arrayV[][] = new double[N][N];
        final double e[] = new double[N];
        final double work[] = new double[N];
        for (i = 0; i < N; i++) {
            array[i][0] = matrix[i][0];
            for (j = 1; j < N; j++) array[i][j] = matrix[i][j];
        }
        for (k = 0; k < Nm1; k++) {
            arrayS[k] = array[k][k];
            for (i = k + 1; i < N; i++) arrayS[k] = ExtraMath.hypot(arrayS[k], array[i][k]);
            if (arrayS[k] != 0.0) {
                if (array[k][k] < 0.0) arrayS[k] = -arrayS[k];
                for (i = k; i < N; i++) array[i][k] /= arrayS[k];
                array[k][k] += 1.0;
            }
            arrayS[k] = -arrayS[k];
            for (j = k + 1; j < N; j++) {
                if (arrayS[k] != 0.0) {
                    double t = 0.0;
                    for (i = k; i < N; i++) t += array[i][k] * array[i][j];
                    t /= -array[k][k];
                    for (i = k; i < N; i++) array[i][j] += t * array[i][k];
                }
                e[j] = array[k][j];
            }
            for (i = k; i < N; i++) arrayU[i][k] = array[i][k];
            if (k < N - 2) {
                e[k] = e[k + 1];
                for (i = k + 2; i < N; i++) e[k] = ExtraMath.hypot(e[k], e[i]);
                if (e[k] != 0.0) {
                    if (e[k + 1] < 0.0) e[k] = -e[k];
                    for (i = k + 1; i < N; i++) e[i] /= e[k];
                    e[k + 1] += 1.0;
                }
                e[k] = -e[k];
                if (e[k] != 0.0) {
                    for (i = k + 1; i < N; i++) {
                        work[i] = 0.0;
                        for (j = k + 1; j < N; j++) work[i] += e[j] * array[i][j];
                    }
                    for (j = k + 1; j < N; j++) {
                        double t = -e[j] / e[k + 1];
                        for (i = k + 1; i < N; i++) array[i][j] += t * work[i];
                    }
                }
                for (i = k + 1; i < N; i++) arrayV[i][k] = e[i];
            }
        }
        int p = N;
        arrayS[Nm1] = array[Nm1][Nm1];
        e[N - 2] = array[N - 2][Nm1];
        e[Nm1] = 0.0;
        for (i = 0; i < N; i++) arrayU[i][Nm1] = 0.0;
        arrayU[Nm1][Nm1] = 1.0;
        for (k = N - 2; k >= 0; k--) {
            if (arrayS[k] != 0.0) {
                for (j = k + 1; j < N; j++) {
                    double t = arrayU[k][k] * arrayU[k][j];
                    for (i = k + 1; i < N; i++) t += arrayU[i][k] * arrayU[i][j];
                    t /= -arrayU[k][k];
                    for (i = k; i < N; i++) arrayU[i][j] += t * arrayU[i][k];
                }
                for (i = k; i < N; i++) arrayU[i][k] = -arrayU[i][k];
                arrayU[k][k] += 1.0;
                for (i = 0; i < k - 1; i++) arrayU[i][k] = 0.0;
            } else {
                for (i = 0; i < N; i++) arrayU[i][k] = 0.0;
                arrayU[k][k] = 1.0;
            }
        }
        for (k = Nm1; k >= 0; k--) {
            if (k < N - 2 && e[k] != 0.0) {
                for (j = k + 1; j < N; j++) {
                    double t = arrayV[k + 1][k] * arrayV[k + 1][j];
                    for (i = k + 2; i < N; i++) t += arrayV[i][k] * arrayV[i][j];
                    t /= -arrayV[k + 1][k];
                    for (i = k + 1; i < N; i++) arrayV[i][j] += t * arrayV[i][k];
                }
            }
            for (i = 0; i < N; i++) arrayV[i][k] = 0.0;
            arrayV[k][k] = 1.0;
        }
        final double eps = Math.pow(2.0, -52.0);
        int iter = 0;
        while (p > 0) {
            int action;
            for (k = p - 2; k >= -1; k--) {
                if (k == -1) break;
                if (Math.abs(e[k]) <= eps * (Math.abs(arrayS[k]) + Math.abs(arrayS[k + 1]))) {
                    e[k] = 0.0;
                    break;
                }
            }
            if (k == p - 2) {
                action = 4;
            } else {
                int ks;
                for (ks = p - 1; ks >= k; ks--) {
                    if (ks == k) break;
                    double t = (ks != p ? Math.abs(e[ks]) : 0.0) + (ks != k + 1 ? Math.abs(e[ks - 1]) : 0.0);
                    if (Math.abs(arrayS[ks]) <= eps * t) {
                        arrayS[ks] = 0.0;
                        break;
                    }
                }
                if (ks == k) {
                    action = 3;
                } else if (ks == p - 1) {
                    action = 1;
                } else {
                    action = 2;
                    k = ks;
                }
            }
            k++;
            switch(action) {
                case 1:
                    {
                        double f = e[p - 2];
                        e[p - 2] = 0.0;
                        for (j = p - 2; j >= k; j--) {
                            double t = ExtraMath.hypot(arrayS[j], f);
                            final double cs = arrayS[j] / t;
                            final double sn = f / t;
                            arrayS[j] = t;
                            if (j != k) {
                                f = -sn * e[j - 1];
                                e[j - 1] *= cs;
                            }
                            for (i = 0; i < N; i++) {
                                t = cs * arrayV[i][j] + sn * arrayV[i][p - 1];
                                arrayV[i][p - 1] = -sn * arrayV[i][j] + cs * arrayV[i][p - 1];
                                arrayV[i][j] = t;
                            }
                        }
                    }
                    break;
                case 2:
                    {
                        double f = e[k - 1];
                        e[k - 1] = 0.0;
                        for (j = k; j < p; j++) {
                            double t = ExtraMath.hypot(arrayS[j], f);
                            final double cs = arrayS[j] / t;
                            final double sn = f / t;
                            arrayS[j] = t;
                            f = -sn * e[j];
                            e[j] *= cs;
                            for (i = 0; i < N; i++) {
                                t = cs * arrayU[i][j] + sn * arrayU[i][k - 1];
                                arrayU[i][k - 1] = -sn * arrayU[i][j] + cs * arrayU[i][k - 1];
                                arrayU[i][j] = t;
                            }
                        }
                    }
                    break;
                case 3:
                    {
                        final double scale = Math.max(Math.max(Math.max(Math.max(Math.abs(arrayS[p - 1]), Math.abs(arrayS[p - 2])), Math.abs(e[p - 2])), Math.abs(arrayS[k])), Math.abs(e[k]));
                        double sp = arrayS[p - 1] / scale;
                        double spm1 = arrayS[p - 2] / scale;
                        double epm1 = e[p - 2] / scale;
                        double sk = arrayS[k] / scale;
                        double ek = e[k] / scale;
                        double b = ((spm1 + sp) * (spm1 - sp) + epm1 * epm1) / 2.0;
                        double c = (sp * epm1) * (sp * epm1);
                        double shift = 0.0;
                        if (b != 0.0 || c != 0.0) {
                            shift = Math.sqrt(b * b + c);
                            if (b < 0.0) shift = -shift;
                            shift = c / (b + shift);
                        }
                        double f = (sk + sp) * (sk - sp) + shift;
                        double g = sk * ek;
                        for (j = k; j < p - 1; j++) {
                            double t = ExtraMath.hypot(f, g);
                            double cs = f / t;
                            double sn = g / t;
                            if (j != k) e[j - 1] = t;
                            f = cs * arrayS[j] + sn * e[j];
                            e[j] = cs * e[j] - sn * arrayS[j];
                            g = sn * arrayS[j + 1];
                            arrayS[j + 1] *= cs;
                            for (i = 0; i < N; i++) {
                                t = cs * arrayV[i][j] + sn * arrayV[i][j + 1];
                                arrayV[i][j + 1] = -sn * arrayV[i][j] + cs * arrayV[i][j + 1];
                                arrayV[i][j] = t;
                            }
                            t = ExtraMath.hypot(f, g);
                            cs = f / t;
                            sn = g / t;
                            arrayS[j] = t;
                            f = cs * e[j] + sn * arrayS[j + 1];
                            arrayS[j + 1] = -sn * e[j] + cs * arrayS[j + 1];
                            g = sn * e[j + 1];
                            e[j + 1] *= cs;
                            if (j < Nm1) {
                                for (i = 0; i < N; i++) {
                                    t = cs * arrayU[i][j] + sn * arrayU[i][j + 1];
                                    arrayU[i][j + 1] = -sn * arrayU[i][j] + cs * arrayU[i][j + 1];
                                    arrayU[i][j] = t;
                                }
                            }
                        }
                        e[p - 2] = f;
                        iter++;
                    }
                    break;
                case 4:
                    {
                        if (arrayS[k] <= 0.0) {
                            arrayS[k] = -arrayS[k];
                            for (i = 0; i < p; i++) arrayV[i][k] = -arrayV[i][k];
                        }
                        while (k < p - 1) {
                            if (arrayS[k] >= arrayS[k + 1]) break;
                            double tmp = arrayS[k];
                            arrayS[k] = arrayS[k + 1];
                            arrayS[k + 1] = tmp;
                            if (k < Nm1) {
                                for (i = 0; i < N; i++) {
                                    tmp = arrayU[i][k + 1];
                                    arrayU[i][k + 1] = arrayU[i][k];
                                    arrayU[i][k] = tmp;
                                    tmp = arrayV[i][k + 1];
                                    arrayV[i][k + 1] = arrayV[i][k];
                                    arrayV[i][k] = tmp;
                                }
                            }
                            k++;
                        }
                        iter = 0;
                        p--;
                    }
                    break;
            }
        }
        final DoubleSquareMatrix svd[] = new DoubleSquareMatrix[3];
        svd[0] = new DoubleSquareMatrix(arrayU);
        svd[1] = new DoubleDiagonalMatrix(arrayS);
        svd[2] = new DoubleSquareMatrix(arrayV);
        return svd;
    }
}
