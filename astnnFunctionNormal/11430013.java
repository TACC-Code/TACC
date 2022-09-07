class BackupThread extends Thread {
    public void format(InputStream is, OutputStream os) throws ReadStreamException, WriteStreamException, WrongBracketAmountException {
        int symbol = 0;
        Reader reader = new Reader(is);
        Writer writer = new Writer(os);
        try {
            while ((symbol = reader.readNext()) != -1) {
                for (int i = 0; i < SymbolFormatter.amount(); i++) {
                    if (symbols[i].isThisSymbol(symbol)) {
                        symbols[i].format(reader, writer, state);
                        break;
                    }
                }
            }
            if (state.getIndentAmount() != 0) throw new WrongBracketAmountException(state.getIndentAmount() + " brackets not closed");
        } catch (ReadStreamException e) {
            throw e;
        } catch (WriteStreamException e) {
            throw e;
        }
    }
}
