class BackupThread extends Thread {
    public void run() {
        String symbol = security.getLevel2Feed().getSymbol();
        if (symbol == null || symbol.length() == 0) symbol = security.getCode();
        try {
            URL url = new URL("http://bvserver.island.com/SERVICE/SQUOTE?STOCK=" + symbol);
            inputStream = url.openStream();
            while (!stopping) {
                if (inputStream.available() == 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                    continue;
                }
                int i = inputStream.read();
                if (i <= 0) break;
                switch(i) {
                    case 72:
                        readNum(inputStream, 3);
                        break;
                    case 83:
                        readNum(inputStream, 3);
                        readStockData();
                        break;
                    case 78:
                        break;
                }
            }
            inputStream.close();
        } catch (Exception e) {
            CorePlugin.logException(e);
        }
        thread = null;
    }
}
