class BackupThread extends Thread {
    static Object getIRR(double[] var) {
        Complex[] zeros = null;
        int pLife = var.length - 1;
        int notNull = pLife;
        while ((var[notNull] == 0.0) && (notNull > 0)) {
            notNull -= 1;
        }
        double[] newVar = new double[notNull];
        for (int i = 0; i < notNull; i++) {
            newVar[i] = var[i + 1];
        }
        Polynomial poly = new Polynomial(newVar);
        try {
            zeros = poly.zeros();
        } catch (RootException e) {
            System.out.println(e.getMessage());
        }
        ArrayList<Double> realRates = new ArrayList<Double>();
        if (zeros != null) {
            for (int i = 0; i < zeros.length; i++) {
                if ((zeros[i].im() == 0.0) && (zeros[i].re() != 0.0)) {
                    Double IRR;
                    IRR = 100 * (1 - zeros[i].re()) / zeros[i].re();
                    realRates.add(IRR);
                }
            }
            if (realRates.size() == 1) {
                return realRates.get(0);
            } else if (realRates.size() > 1) {
                return "More than one rate found: rate of return not applicable";
            } else return "No rate of return could be found";
        } else return "No roots could be found: rate of return not applicable";
    }
}
