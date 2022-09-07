class BackupThread extends Thread {
        protected void transfer(Pipe pipe, Input input, Output output) throws IOException {
            for (int number = input.readFieldNumber(wrappedSchema); ; number = input.readFieldNumber(wrappedSchema)) {
                switch(number) {
                    case 0:
                        return;
                    case 1:
                        output.writeInt32(number, input.readInt32(), true);
                        break;
                    case 2:
                        input.transferByteRangeTo(output, true, number, true);
                        break;
                    case 3:
                        output.writeObject(number, pipe, Bar.getPipeSchema(), true);
                        break;
                    case 4:
                        output.writeEnum(number, input.readEnum(), true);
                        break;
                    case 5:
                        input.transferByteRangeTo(output, false, number, true);
                        break;
                    case 6:
                        output.writeBool(number, input.readBool(), true);
                        break;
                    case 7:
                        output.writeFloat(number, input.readFloat(), true);
                        break;
                    case 8:
                        output.writeDouble(number, input.readDouble(), true);
                        break;
                    case 9:
                        output.writeInt64(number, input.readInt64(), true);
                        break;
                    default:
                        input.handleUnknownField(number, wrappedSchema);
                }
            }
        }
}
