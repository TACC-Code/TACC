class BackupThread extends Thread {
    protected int cat(Reader plainInput, String infileName, Writer output, int lineNumber) throws IOException {
        LineNumberReader input = new LineNumberReader(new BufferedReader(plainInput));
        output = new BufferedWriter(output);
        boolean outputTabs = isOutputTabs();
        boolean numbersAtEmptyLines = isNumbersAtEmptyLines();
        boolean markLineEnds = isMarkLineEnds();
        boolean squeezeEmptyLines = isSqueezeEmptyLines();
        boolean number = isNumbers();
        boolean quote = isQuote();
        int newlines = getNewlines();
        input.setLineNumber(lineNumber);
        int ch = input.read();
        boolean startOfFile = true;
        while (ch != -1) {
            if (numbers && ch != '\n') {
                output.write(nextLineNum(lineNumber, output));
                output.write('\t');
            }
            if (quote && ch != '\n') {
                while (ch != -1) {
                    if (ch >= 32) {
                        if (ch < 127) {
                            output.write(ch);
                        } else if (ch == 127) {
                            output.write("^?");
                        } else {
                            if (ch >= 128 + 32) {
                            } else {
                                output.write('^');
                                output.write(ch - 128 + 64);
                            }
                        }
                    } else if (ch == '\t' && outputTabs) {
                        output.write('\t');
                    } else if (ch == '\n') {
                        if (!startOfFile) newlines = 0;
                        break;
                    } else {
                        output.write('^');
                        output.write(ch + 64);
                    }
                    ch = input.read();
                    if (startOfFile) startOfFile = false;
                }
            } else if (ch != '\n') {
                while (ch != -1) {
                    if (ch == '\t' && !outputTabs) {
                        output.write("^I");
                    } else if (ch == '\n') {
                        if (!startOfFile) newlines = 0;
                        break;
                    } else {
                        output.write(ch);
                    }
                    ch = input.read();
                    if (startOfFile) startOfFile = false;
                }
            }
            while (ch == '\n') {
                if (++newlines > 1) {
                    if (squeezeEmptyLines && (newlines > 2)) {
                        ch = input.read();
                        input.setLineNumber(input.getLineNumber() - 1);
                        continue;
                    }
                    if (numbers && numbersAtEmptyLines) {
                        output.write(nextLineNum(lineNumber, output));
                        output.write('\t');
                    }
                    if (!numbersAtEmptyLines) input.setLineNumber(input.getLineNumber() - 1);
                }
                if (markLineEnds) output.write('$');
                output.write('\n');
                lineNumber = input.getLineNumber();
                ch = input.read();
            }
        }
        output.flush();
        setNewlines(newlines);
        return lineNumber;
    }
}
