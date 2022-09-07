class BackupThread extends Thread {
    @Override
    public void backTransform(double[][] sourcePoints, double[][] points, InkTraceFormat canvasFormat, InkTraceFormat sourceFormat) throws InkMLComplianceException {
        int[] targetIndices = new int[sourceFormat.getChannelCount()];
        int i = 0;
        for (InkChannel c : sourceFormat) {
            targetIndices[i++] = canvasFormat.indexOf(c.getName());
        }
        for (i = 0; i < sourcePoints.length; i++) {
            for (int c = 0; c < targetIndices.length; c++) {
                sourcePoints[i][c] = points[i][targetIndices[c]];
            }
        }
    }
}
