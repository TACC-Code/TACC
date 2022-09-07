class BackupThread extends Thread {
    protected void sendPatchWorker(Patch p, int patchnum) {
        for (int offset = 0; offset <= p.sysex.length - YamahaMotifSysexUtility.SYSEX_OVERHEAD; offset += p.sysex.length - YamahaMotifSysexUtility.SYSEX_OVERHEAD) {
            p.sysex[offset + YamahaMotifSysexUtility.ADDRESS_OFFSET + 1] = (patchnum == -1) ? Byte.parseByte(edit_buffer_base_address, 16) : Byte.parseByte(base_address, 16);
            p.sysex[offset + YamahaMotifSysexUtility.ADDRESS_OFFSET + 2] = (byte) ((patchnum == -1) ? 0 : (patchnum & 128));
            YamahaMotifSysexUtility.checksum(p.sysex, offset);
        }
        YamahaMotifSysexUtility.splitAndSendBulk(p.sysex, this, getChannel() - 1);
        p.sysex[YamahaMotifSysexUtility.ADDRESS_OFFSET + 1] = Byte.parseByte(base_address, 16);
    }
}
