class BackupThread extends Thread {
    public void visit(AVRInstr.ROL i) {
        $write_int8(i.rd, performLeftShift($read_int8(i.rd) & 255, C));
    }
}
