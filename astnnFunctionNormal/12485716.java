class BackupThread extends Thread {
    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) < 2) throwMathLibException("sprintf: number of input arguments <2");
        if (!(operands[0] instanceof CharToken)) throwMathLibException("sprintf: format must be a string");
        formatS = ((CharToken) operands[0]).getValue();
        tok = new Token[operands.length - 1];
        for (int i = 0; i < (operands.length - 1); i++) tok[i] = operands[i + 1];
        while (EOL == false) {
            char c = getNextChar();
            switch(c) {
                case '%':
                    {
                        parseFormat();
                        break;
                    }
                default:
                    {
                        retString = retString + c;
                        ErrorLogger.debugLine("sprintf: " + retString);
                    }
            }
        }
        return new CharToken(retString);
    }
}
