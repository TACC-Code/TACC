class BackupThread extends Thread {
    private void loadProfileDataFile(File file) {
        FileInputStream fileIn;
        try {
            ObjectInputStream in = new ObjectInputStream(fileIn = new FileInputStream(file));
            FileChannel fileChannel = fileIn.getChannel();
            while (fileChannel.position() < fileChannel.size()) {
                Object obj = null;
                try {
                    obj = in.readObject();
                    if (obj instanceof RawProfileReport) {
                        RawProfileReport r = (RawProfileReport) obj;
                        reports.add(new ProfileReportKey(r.getStartTime(), true, r));
                        reports.add(new ProfileReportKey(r.getEndTime(), false, r));
                        if (r.wasTransactional) {
                            backlog.put(r.txnId, r);
                        }
                        beginning_of_time = Math.min(beginning_of_time, r.getStartTime());
                        end_of_time = Math.max(end_of_time, r.getEndTime());
                    } else if (obj instanceof RawPropertyChangeEvent) {
                        RawPropertyChangeEvent event = (RawPropertyChangeEvent) obj;
                        System.out.println("event: " + event);
                    } else {
                        System.out.println("Unknown obj: " + obj + "[" + obj.getClass() + "]");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
