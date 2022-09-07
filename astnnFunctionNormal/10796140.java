class BackupThread extends Thread {
    static void transferObject(Pipe.Schema<Object> pipeSchema, Pipe pipe, Input input, Output output, IdStrategy strategy) throws IOException {
        final int number = input.readFieldNumber(pipeSchema.wrappedSchema);
        switch(number) {
            case ID_EMPTY_MAP:
                output.writeUInt32(number, input.readUInt32(), false);
                break;
            case ID_SINGLETON_MAP:
                if (0 != input.readUInt32()) throw new ProtostuffException("Corrupt input.");
                output.writeUInt32(number, 0, false);
                transferSingletonMap(pipeSchema, pipe, input, output, strategy);
                return;
            case ID_UNMODIFIABLE_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;
            case ID_UNMODIFIABLE_SORTED_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;
            case ID_SYNCHRONIZED_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;
            case ID_SYNCHRONIZED_SORTED_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                break;
            case ID_CHECKED_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                if (1 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                output.writeObject(1, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                if (2 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                output.writeObject(2, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                break;
            case ID_CHECKED_SORTED_MAP:
                output.writeObject(number, pipe, strategy.POLYMORPHIC_MAP_PIPE_SCHEMA, false);
                if (1 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                output.writeObject(1, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                if (2 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
                output.writeObject(2, pipe, strategy.CLASS_PIPE_SCHEMA, false);
                break;
            case ID_ENUM_MAP:
                strategy.transferEnumId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.MAP_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            case ID_MAP:
                strategy.transferMapId(input, output, number);
                if (output instanceof StatefulOutput) {
                    ((StatefulOutput) output).updateLast(strategy.MAP_PIPE_SCHEMA, pipeSchema);
                }
                Pipe.transferDirect(strategy.MAP_PIPE_SCHEMA, pipe, input, output);
                return;
            default:
                throw new ProtostuffException("Corrupt input.");
        }
        if (0 != input.readFieldNumber(pipeSchema.wrappedSchema)) throw new ProtostuffException("Corrupt input.");
    }
}
