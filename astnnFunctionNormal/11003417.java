class BackupThread extends Thread {
    @Override
    public void transform(double[][] sourcePoints, double[][] points, InkTraceFormat sourceFormat, InkTraceFormat targetFormat) throws InkMLComplianceException {
        int[] targetIndices = new int[sourceFormat.getChannelCount()];
        int i = 0;
        for (InkChannel c : sourceFormat) {
            targetIndices[i++] = targetFormat.indexOf(c.getName());
        }
        for (i = 0; i < sourcePoints.length; i++) {
            for (int c = 0; c < targetIndices.length; c++) {
                points[i][targetIndices[c]] = sourcePoints[i][c];
            }
        }
    }
}
