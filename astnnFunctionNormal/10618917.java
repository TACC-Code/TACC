class BackupThread extends Thread {
        @Override
        void copySecKey(RecordInput input, RecordOutput output) {
            output.writeFast(input.readFast());
            output.writeFast(input.readFast());
            output.writeFast(input.readFast());
            output.writeFast(input.readFast());
        }
}
