class BackupThread extends Thread {
    private void generateShipments() {
        log.info("M_Warehouse_ID=" + m_M_Warehouse_ID);
        String trxName = Trx.createTrxName("IOG");
        Trx trx = Trx.get(trxName, true);
        m_selectionActive = false;
        statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InOutGenerateGen"));
        statusBar.setStatusDB(String.valueOf(selection.size()));
        int AD_Process_ID = 0;
        KeyNamePair docTypeKNPair = (KeyNamePair) cmbDocType.getSelectedItem();
        if (docTypeKNPair.getKey() == MRMA.Table_ID) {
            AD_Process_ID = 52001;
        } else {
            AD_Process_ID = 199;
        }
        MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
        if (!instance.save()) {
            info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }
        StringBuffer insert = new StringBuffer();
        insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
        int counter = 0;
        for (Integer selectedId : selection) {
            counter++;
            if (counter > 1) insert.append(" UNION ");
            insert.append("SELECT ");
            insert.append(instance.getAD_PInstance_ID());
            insert.append(", ");
            insert.append(selectedId);
            insert.append(" FROM DUAL ");
            if (counter == 1000) {
                if (DB.executeUpdate(insert.toString(), trxName) < 0) {
                    String msg = "No Shipments";
                    log.config(msg);
                    info.setText(msg);
                    trx.rollback();
                    return;
                }
                insert = new StringBuffer();
                insert.append("INSERT INTO T_SELECTION(AD_PINSTANCE_ID, T_SELECTION_ID) ");
                counter = 0;
            }
        }
        if (counter > 0) {
            if (DB.executeUpdate(insert.toString(), trxName) < 0) {
                String msg = "No Shipments";
                log.config(msg);
                info.setText(msg);
                trx.rollback();
                return;
            }
        }
        ProcessInfo pi = new ProcessInfo("VInOutGen", AD_Process_ID);
        pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
        MPInstancePara ip = new MPInstancePara(instance, 10);
        ip.setParameter("Selection", "Y");
        if (!ip.save()) {
            String msg = "No Parameter added";
            info.setText(msg);
            log.log(Level.SEVERE, msg);
            return;
        }
        ip = new MPInstancePara(instance, 20);
        ip.setParameter("M_Warehouse_ID", Integer.parseInt(m_M_Warehouse_ID.toString()));
        if (!ip.save()) {
            String msg = "No Parameter added";
            info.setText(msg);
            log.log(Level.SEVERE, msg);
            return;
        }
        ProcessCtl worker = new ProcessCtl(this, Env.getWindowNo(this), pi, trx);
        worker.start();
    }
}
