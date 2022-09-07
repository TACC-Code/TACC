class BackupThread extends Thread {
    public Symbol makeTransactional(String symbolName) throws ObolException {
        Symbol _sym = null;
        if (this.transactionActive) {
            Map _currentMap = (Map) this.sessions.peek();
            _sym = this.findSymbol(symbolName);
            if (null != _sym && false == _currentMap.containsKey(symbolName)) {
                if ((null == _sym.getValue()) || _sym.isAnonymous()) {
                    try {
                        log.debug(__me + ".getSymbol(): making transaction clone of \"" + symbolName + "\"");
                        _sym = (Symbol) _sym.clone();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(__me + ".getSymbol(\"" + symbolName + "\"): clone failed: " + e);
                    }
                    _currentMap.put(symbolName, _sym);
                } else {
                    throw new ObolException(__me + ".makeTransactional(): " + "Illogical attempt of making transaction write-copy " + "of read-only symbol \"" + symbolName + "\"!");
                }
            } else {
                _sym.incForcedCount();
            }
        }
        if (null == _sym) {
            _sym = this.getSymbol(symbolName);
        }
        return _sym;
    }
}
