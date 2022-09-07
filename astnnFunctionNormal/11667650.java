class BackupThread extends Thread {
    public void visit(AVRInstr.ORI i) {
        $write_int8(i.rd, performOr($read_int8(i.rd), i.imm.value));
    }
}
