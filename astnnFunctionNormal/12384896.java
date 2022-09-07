class BackupThread extends Thread {
    public void writeChart(BufferedWriter out) {
        FortranNumberFormat fmtT = new FortranNumberFormat("G20.13");
        FortranNumberFormat fmtY = new FortranNumberFormat("G10.3");
        try {
            int nPVs = spvV.size();
            if (nPVs <= 0) {
                return;
            }
            out.newLine();
            String names = "% ";
            for (int i = 0, n = nPVs; i < n; i++) {
                names = names + " " + ((ScalarPV) spvV.get(i)).getMonitoredPV().getChannelName();
            }
            out.write(names);
            out.newLine();
            out.write("%==================time chart data=================================");
            out.newLine();
            int nL = ((ScalarPV) spvV.get(0)).getValueChartGraphData().getSize();
            for (int il = 0; il < nL; il++) {
                double time = ((ScalarPV) spvV.get(0)).getValueChartGraphData().getX(il);
                out.write(" " + fmtT.format(time) + " ");
                for (int i = 0, n = nPVs; i < n; i++) {
                    CurveData cv = ((ScalarPV) spvV.get(i)).getValueChartGraphData();
                    double y = cv.getY(il);
                    out.write(" " + fmtY.format(y));
                }
                out.newLine();
            }
        } catch (IOException exception) {
            messageTextLocal.setText(null);
            messageTextLocal.setText("Fatal error. Something wrong with output file. Stop.");
        }
    }
}
