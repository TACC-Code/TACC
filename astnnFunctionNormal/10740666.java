class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                if (pvn_parent == parameterPV_Node) {
                    parameterPV_Controller.setChannel(pvn.getChannel());
                }
                if (pvn_parent == parameterPV_RB_Node) {
                    parameterPV_Controller.setChannelRB(pvn.getChannel());
                }
                if (pvn_parent == scanPV_Node) {
                    scanVariable.setChannel(pvn.getChannel());
                    graphScan.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                    graphAnalysis.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                    graphScan.refreshGraphJPanel();
                    graphAnalysis.refreshGraphJPanel();
                }
                if (pvn_parent == scanPV_RB_Node) {
                    scanVariable.setChannelRB(pvn.getChannel());
                }
                if (pvn_parent == measuredPVs_Node) {
                    index = pvn_parent.getIndex(pvn);
                    MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(index);
                    MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                    mpv_tmp.setChannel(pvn.getChannel());
                }
                if (pvn_parent == validationPVs_Node) {
                    index = pvn_parent.getIndex(pvn);
                    MeasuredValue mv_tmp = (MeasuredValue) validationValuesV.get(index);
                    MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                    mpv_tmp.setChannel(pvn.getChannel());
                }
            }
}
