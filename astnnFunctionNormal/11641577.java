class BackupThread extends Thread {
    void readSensor() {
        String openURL = "sensor:";
        switch(sensors_set.getSelectedIndex()) {
            case 0:
                openURL += "battery_level";
                break;
            case 1:
                openURL += "battery_charge";
                break;
        }
        try {
            conn = (SensorConnection) Connector.open(openURL);
        } catch (Throwable ex) {
            getAlert().setString("Sensor open error: " + ex.getMessage());
            switchDisplayable(null, alert);
            return;
        }
        int buffSize = Integer.parseInt(buffer_size.getString());
        long period = Long.parseLong(buffering_period.getString());
        try {
            switch(sync_async.getSelectedIndex()) {
                case 0:
                    retData = conn.getData(buffSize, period, true, true, true);
                    break;
                case 1:
                    conn.setDataListener((DataListener) this, buffSize, period, true, true, true);
                    synchronized (this) {
                        wait();
                    }
                    break;
            }
        } catch (Exception ex) {
            try {
                conn.close();
            } catch (IOException exc) {
            }
            getAlert().setString("Sensor reading error: " + ex.getMessage());
            switchDisplayable(null, alert);
            return;
        }
        int minValue = (int) conn.getSensorInfo().getChannelInfos()[0].getMeasurementRanges()[0].getSmallestValue();
        int maxValue = (int) conn.getSensorInfo().getChannelInfos()[0].getMeasurementRanges()[0].getLargestValue();
        try {
            conn.close();
        } catch (IOException ex) {
        }
        String outp = "";
        for (int i = 0; i < retData.length; i++) {
            outp += "Channel " + i + ":\n";
            int[] chData = retData[i].getIntValues();
            for (int j = 0; j < chData.length; j++) {
                outp += chData[j] + " ";
            }
            outp += "\n";
        }
        getData_from_sensor().setText(outp);
        getSignal_diagram().setMaxValue(maxValue - minValue);
        getSignal_diagram().setValue(retData[0].getIntValues()[0]);
        switchDisplayable(null, getData_display());
    }
}
