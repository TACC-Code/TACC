class BackupThread extends Thread {
    public boolean fetchFixedBlock(BufferedReader reader, int blockSize) {
        Date start = new Date();
        try {
            String text = null;
            if (mUrl2DocnoMap.size() == 0) {
                mBlockNumber++;
            }
            int number = 0;
            System.out.println("Begin reading block " + mBlockNumber + " ...");
            while (number++ < blockSize && (text = reader.readLine()) != null) {
                String fields[] = text.split("\t");
                if (fields == null || fields.length != 2) {
                    String msg = "Invliad format with text =  " + text;
                    System.err.println(msg);
                } else {
                    mUrl2DocnoMap.put(MD5.digest(fields[0]), fields[1]);
                }
            }
            Date end = new Date();
            System.out.println("Finished reading block " + mBlockNumber + ", with block size = " + mUrl2DocnoMap.size() + " and time spent: " + (end.getTime() - start.getTime()) / 1000 + " ms");
            if (text != null) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
