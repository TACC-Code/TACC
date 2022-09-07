class BackupThread extends Thread {
    private void savePendingTrendDiagrams() {
        try {
            while (!trendData[0].isEmpty()) {
                for (int j = 0; j < trendData.length; j++) {
                    trendMitMgr.getDataFile().getOutputStream().write(trendData[j].readSingle());
                }
            }
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error writting pending trend diagrams", e);
        }
    }
}
