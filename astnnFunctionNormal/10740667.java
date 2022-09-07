class BackupThread extends Thread {
    private void updateDataSetOnGraphPanels() {
        graphScan.removeAllGraphData();
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            if (((Boolean) measuredValuesShowStateV.get(i)).booleanValue()) {
                MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(i);
                if (scanPV_ShowState || scanVariable.getChannel() == null) {
                    graphScan.addGraphData(mv_tmp.getDataContainers());
                }
                if (scanPV_RB_ShowState) {
                    graphScan.addGraphData(mv_tmp.getDataContainersRB());
                }
            }
        }
        analysisController.setScanPVandScanPV_RB_State(scanPV_ShowState, scanPV_RB_ShowState);
        analysisController.updateDataSetOnGraphPanel();
    }
}
