class BackupThread extends Thread {
    public String getStringInput(String _inscheme) throws SchemeException {
        incomingDataLen += _inscheme.length();
        incomingLinesIndex++;
        InputPort inp = new InputPort(new StringReader(_inscheme));
        StringWriter _strOut = new StringWriter();
        PrintWriter _outWriter = new PrintWriter(new BufferedWriter(_strOut));
        U.write(eval(inp.read()), _outWriter, false);
        inp = new InputPort(new StringReader(_inscheme));
        _outWriter.flush();
        if (logMessagesFlag) {
            logSchemeMessages.append(_inscheme + "\n");
        }
        return _strOut.toString();
    }
}
