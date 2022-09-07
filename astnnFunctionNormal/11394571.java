class BackupThread extends Thread {
    public ClassicToken(Token oldToken) {
        text = oldToken.getText();
        type = oldToken.getType();
        line = oldToken.getLine();
        charPositionInLine = oldToken.getCharPositionInLine();
        channel = oldToken.getChannel();
    }
}
