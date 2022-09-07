class BackupThread extends Thread {
    public void visit(AVRInstr.MOVW i) {
        $write_uint16(i.rd, $read_uint16(i.rr));
    }
}
