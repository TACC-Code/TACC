class BackupThread extends Thread {
    public void visit(AVRInstr.MOV i) {
        $write_int8(i.rd, $read_int8(i.rr));
    }
}
