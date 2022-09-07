class BackupThread extends Thread {
    public void visit(AVRInstr.ROR i) {
        $write_int8(i.rd, performRightShift($read_int8(i.rd), C));
    }
}
