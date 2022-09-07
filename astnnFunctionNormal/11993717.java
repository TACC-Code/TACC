class BackupThread extends Thread {
    public HashMap<String, Double> getBPMXMap() {
        HashMap<String, Double> bpmXMap = new HashMap<String, Double>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            if (css[i].getPV().indexOf("MON:X") > -1) {
                double[] val = css[i].getValue();
                bpmXMap.put(css[i].getPV(), new Double(val[0]));
            }
        }
        return bpmXMap;
    }
}
