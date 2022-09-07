class BackupThread extends Thread {
    private SensorConnection openSensor() {
        infos = SensorManager.findSensors("acceleration", null);
        if (infos.length == 0) return null;
        int datatypes[] = new int[infos.length];
        int i = 0;
        String sensor_url = "";
        {
            System.out.println("Searching TYPE_INT sensor...");
            while (!sensor_found) {
                datatypes[i] = infos[i].getChannelInfos()[0].getDataType();
                if (datatypes[i] == 2) {
                    sensor_url = infos[i].getUrl();
                    System.out.println("Sensor: " + sensor_url + ": TYPE_INT found.");
                    sensor_found = true;
                } else i++;
            }
        }
        System.out.println("Sensor: " + sensor_url);
        try {
            return (SensorConnection) Connector.open(sensor_url);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
