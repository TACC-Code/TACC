class BackupThread extends Thread {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException {
            for (int number = input.readFieldNumber(wrappedSchema); ; number = input.readFieldNumber(wrappedSchema)) {
                switch(number) {
                    case 0:
                        return;
                    case 1:
                        output.writeInt32(number, input.readInt32(), false);
                        break;
                    case 2:
                        input.transferByteRangeTo(output, true, number, false);
                        break;
                    case 3:
                        output.writeFixed64(number, input.readFixed64(), false);
                        break;
                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }
        }
}
