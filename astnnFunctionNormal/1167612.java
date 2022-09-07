class BackupThread extends Thread {
    public void callBack(DataPacket dataPkt) {
        System.out.println("DataIntSink:Got some Data");
        for (int i = 0; i < dataPkt.getSize(); i++) {
            double[] data = (double[]) dataPkt.getDataAt(i);
            String chanName = dataPkt.getChannelNameAt(i);
            for (int j = 0; j < data.length; j++) {
                Date timestamp = new Date((long) (dataPkt.getTimestampAt(i, j)));
                String time = DateFormat.getDateTimeInstance().format(timestamp);
                System.out.println("Received|" + sink.getName() + "|" + chanName + "|" + time + "|" + "|" + j + "|" + data[j]);
                updateIntoDb(chanName, (Object) new Double(data[j]), timestamp.getTime());
            }
        }
        System.out.println("-----------------------------------");
    }
}
