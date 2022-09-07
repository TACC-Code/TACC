class BackupThread extends Thread {
    public int getnpcs(double[] E) {
        int npcs = 0;
        double total = 0;
        double[] g = new double[E.length + 1];
        g[E.length] = 0;
        for (int i = E.length - 1; i > 0; i--) {
            total += E[i];
            g[i] = g[i + 1] + 1 / (i * E.length);
        }
        for (int i = 0; i < E.length; i++) if ((E[i] / total) <= g[i]) return (i + 2);
        return E.length;
    }
}
