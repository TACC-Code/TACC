class BackupThread extends Thread {
    public CommonToken(Token oldToken) {
        text = oldToken.getText();
        type = oldToken.getType();
        line = oldToken.getLine();
        index = oldToken.getTokenIndex();
        charPositionInLine = oldToken.getCharPositionInLine();
        channel = oldToken.getChannel();
        if (oldToken instanceof CommonToken) {
            start = ((CommonToken) oldToken).start;
            stop = ((CommonToken) oldToken).stop;
        }
    }
}
