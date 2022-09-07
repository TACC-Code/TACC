class BackupThread extends Thread {
    public void visit(AVRInstr.ADC i) {
        $write_int8(i.rd, performAddition($read_int8(i.rd) & 255, $read_int8(i.rr) & 255, bit(C)));
    }
}
