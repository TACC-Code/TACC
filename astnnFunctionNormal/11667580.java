class BackupThread extends Thread {
    public void visit(AVRInstr.AND i) {
        $write_int8(i.rd, performAnd($read_int8(i.rd), $read_int8(i.rr)));
    }
}
