class BackupThread extends Thread {
    public HashMap<String, Double> getQuadPSMap() {
        HashMap<String, Double> pvMap = new HashMap<String, Double>();
        ChannelSnapshot[] css = mss.getChannelSnapshots();
        for (int i = 0; i < css.length; i++) {
            if ((css[i].getPV().indexOf("MON:G") > -1) || (css[i].getPV().indexOf("SET:G") > -1)) {
                double[] val = css[i].getValue();
                pvMap.put(css[i].getPV(), new Double(val[0]));
            }
        }
        return pvMap;
    }
}
