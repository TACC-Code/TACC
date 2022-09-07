class BackupThread extends Thread {
    public void run() {
        Channel[] ca = new Channel[myDoc.numberOfCav];
        try {
            for (int i = 0; i < myDoc.numberOfCav; i++) {
                ca[i] = cf.getChannel("SCL_HPRF:Tun" + myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Tun_Ctl");
                ca[i].putVal("Off");
            }
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error in connection to PV");
        } catch (PutException pe) {
            myDoc.errormsg("Error in writing to PV");
        }
        while (nottuned) {
            startCorrelator();
            if (cavtuned()) {
                nottuned = false;
                try {
                    for (int i = 0; i < myDoc.numberOfCav; i++) {
                        ca[i].putVal("Auto-Tune");
                    }
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error in connection to PV");
                } catch (PutException pe) {
                    myDoc.errormsg("Error in writing to PV!");
                }
                return;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                myDoc.errormsg("Tune thread was interruped!");
            }
            if (trys > 7) {
                myDoc.errormsg("Failed, need to tune menually");
                return;
            }
        }
        return;
    }
}
