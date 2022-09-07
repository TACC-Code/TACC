class BackupThread extends Thread {
    public void visit(AVRInstr.OR i) {
        $write_int8(i.rd, performOr($read_int8(i.rd), $read_int8(i.rr)));
    }
}
