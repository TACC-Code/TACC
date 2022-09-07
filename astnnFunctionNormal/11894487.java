class BackupThread extends Thread {
    private void connectMons(Channel p_chan) {
        Vector chanVec;
        try {
            chanVec = getChannelVec(p_chan);
            for (int i = 0; i < chanVec.size(); i++) {
                mons.add(p_chan.addMonitorValue((InputPVTableCell) chanVec.elementAt(i), Monitor.VALUE));
            }
            chanVec.removeAllElements();
        } catch (ConnectionException e) {
            System.out.println("Connection Exception");
        } catch (MonitorException e) {
            System.out.println("Monitor Exception");
        }
    }
}
