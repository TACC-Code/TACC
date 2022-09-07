class BackupThread extends Thread {
    public boolean updateChanges(byte[] last, byte[] current, int mysynchDirection, int regsetID) throws IOException {
        int i = 0;
        boolean res = false;
        for (i = 0; i < last.length; i++) {
            if (last[i] != current[i]) {
                res = true;
                if (mysynchDirection == SYNCH_PlC_TO_PC) {
                    last[i] = current[i];
                } else if (mysynchDirection == SYNCH_PC_TO_PLC) {
                    logger.info("--writing because of bit " + i + "  " + last[i] + " " + current[i] + " off " + readOffset[regsetID] + " reglen " + registers[regsetID].length);
                    device.writeMultiReg(readOffset[regsetID], registers[regsetID], registers[regsetID].length);
                    break;
                }
            }
        }
        return res;
    }
}
