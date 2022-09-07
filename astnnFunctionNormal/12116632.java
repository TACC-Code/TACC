class BackupThread extends Thread {
    public void writeBody(DataOutputStream dataOut) throws IOException {
        dataOut.writeByte(mode);
        dataOut.writeInt(databaseID);
        dataOut.writeLong(sessionID);
        dataOut.writeLong(lobID);
        dataOut.writeInt(subType);
        switch(subType) {
            case LobResultTypes.REQUEST_CREATE_BYTES:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.write(stream, blockLength);
                break;
            case LobResultTypes.REQUEST_CREATE_CHARS:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.write(reader, blockLength);
                break;
            case LobResultTypes.REQUEST_SET_BYTES:
            case LobResultTypes.REQUEST_GET_BYTE_PATTERN_POSITION:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.write(byteBlock);
                break;
            case LobResultTypes.REQUEST_SET_CHARS:
            case LobResultTypes.REQUEST_GET_CHAR_PATTERN_POSITION:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.writeChars(charBlock);
                break;
            case LobResultTypes.REQUEST_GET_LOB:
            case LobResultTypes.REQUEST_GET_BYTES:
            case LobResultTypes.REQUEST_GET_CHARS:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                break;
            case LobResultTypes.REQUEST_GET_LENGTH:
            case LobResultTypes.REQUEST_TRUNCATE:
                dataOut.writeLong(blockOffset);
                break;
            case LobResultTypes.RESPONSE_GET_BYTES:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.write(byteBlock);
                break;
            case LobResultTypes.RESPONSE_GET_CHARS:
                dataOut.writeLong(blockOffset);
                dataOut.writeLong(blockLength);
                dataOut.writeChars(charBlock);
                break;
            case LobResultTypes.RESPONSE_SET:
            case LobResultTypes.RESPONSE_CREATE_BYTES:
            case LobResultTypes.RESPONSE_CREATE_CHARS:
            case LobResultTypes.RESPONSE_TRUNCATE:
                dataOut.writeLong(blockLength);
                break;
            case LobResultTypes.RESPONSE_GET_BYTE_PATTERN_POSITION:
            case LobResultTypes.RESPONSE_GET_CHAR_PATTERN_POSITION:
                dataOut.writeLong(blockOffset);
                break;
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "ResultLob");
        }
    }
}
