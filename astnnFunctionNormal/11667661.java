class BackupThread extends Thread {
    public void visit(AVRInstr.SBCI i) {
        $write_int8(i.rd, performSubtractionPZ($read_int8(i.rd), i.imm.value, bit(C)));
    }
}
