class BackupThread extends Thread {
    public void visit(AVRInstr.SBC i) {
        $write_int8(i.rd, performSubtractionPZ($read_int8(i.rd), $read_int8(i.rr), bit(C)));
    }
}
