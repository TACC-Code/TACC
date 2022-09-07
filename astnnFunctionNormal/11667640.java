class BackupThread extends Thread {
    public void visit(AVRInstr.LSL i) {
        $write_int8(i.rd, performLeftShift($read_int8(i.rd), false));
    }
}
