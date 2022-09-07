class BackupThread extends Thread {
    public void visit(AVRInstr.NEG i) {
        $write_int8(i.rd, performSubtraction(0, $read_int8(i.rd), 0));
    }
}
