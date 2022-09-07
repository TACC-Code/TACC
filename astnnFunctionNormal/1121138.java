class BackupThread extends Thread {
    public void run() {
        PacketSource reader = BuildSource.makePacketSource();
        PrintSerialData printData = PrintSerialData.getInstanceOf();
        if (reader == null) {
            System.err.println("Invalid pTestSerialMsgacket source (check your MOTECOM environment variable)");
            System.exit(2);
        }
        try {
            reader.open(PrintStreamMessenger.err);
            while (true) {
                byte[] packet = reader.readPacket();
                printData.print(packet);
                System.out.flush();
                System.out.print("\n");
                if (reader.writePacket(packet)) {
                    System.out.println("packet sent!");
                } else {
                    System.out.println("send packet error!!!");
                }
            }
        } catch (IOException e) {
            System.err.println("Error on " + reader.getName() + ": " + e);
        }
    }
}
