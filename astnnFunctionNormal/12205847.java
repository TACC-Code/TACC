class BackupThread extends Thread {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putChar(message, offset, (char) input.readUInt32()); else us.putObject(message, offset, Character.valueOf((char) input.readUInt32()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeUInt32(number, us.getChar(message, offset), false); else {
                        Character value = (Character) us.getObject(message, offset);
                        if (value != null) output.writeUInt32(number, value.charValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
}
