class BackupThread extends Thread {
    public void callBack(DataPacket dataPkt) {
        System.out.println("DswProcess:Got some Data");
        for (int i = 0; i < dataPkt.getSize(); i++) {
            double[] data = (double[]) dataPkt.getDataAt(i);
            String chanName = dataPkt.getChannelNameAt(i);
            for (int j = 0; j < data.length; j++) {
                String time = DateFormat.getDateTimeInstance().format(new Date((long) (dataPkt.getTimestampAt(i, j))));
                System.out.println("Received|" + helper.sink.getName() + "|" + chanName + "|" + time + "|" + "|" + j + "|" + data[j]);
            }
            String convertedChanName = tempGetChannelName(chanName);
            helper.insertData(sourceNames[0], convertedChanName, (Object) data);
            String time = DateFormat.getDateTimeInstance().format(new Date());
            for (int j = 0; j < data.length; j++) {
                System.out.println("Sent|" + sourceNames[0] + "/" + convertedChanName + "|" + time + System.currentTimeMillis() + "|" + i + "|" + data[j]);
            }
        }
        int numChannelsFlushed = helper.flush(sourceNames[0]);
        String time = DateFormat.getDateTimeInstance().format(new Date());
        System.out.println("Flushed|" + sourceNames[0] + "|" + time + "|" + System.currentTimeMillis());
        System.out.println("-----------------------------------");
    }
}
