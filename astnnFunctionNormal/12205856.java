class BackupThread extends Thread {
        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {
            final boolean primitive = f.getType().isPrimitive();
            final long offset = us.objectFieldOffset(f);
            return new Field<T>(FieldType.UINT32, number, name) {

                public void mergeFrom(Input input, T message) throws IOException {
                    if (primitive) us.putShort(message, offset, (short) input.readUInt32()); else us.putObject(message, offset, Short.valueOf((short) input.readUInt32()));
                }

                public void writeTo(Output output, T message) throws IOException {
                    if (primitive) output.writeUInt32(number, us.getShort(message, offset), false); else {
                        Short value = (Short) us.getObject(message, offset);
                        if (value != null) output.writeUInt32(number, value.shortValue(), false);
                    }
                }

                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {
                    output.writeUInt32(number, input.readUInt32(), repeated);
                }
            };
        }
}
