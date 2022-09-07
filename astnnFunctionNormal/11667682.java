class BackupThread extends Thread {
    public void visit(AVRInstr.SUB i) {
        $write_int8(i.rd, performSubtraction($read_int8(i.rd), $read_int8(i.rr), 0));
    }
}
