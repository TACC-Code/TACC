class BackupThread extends Thread {
    public static void main(String args[]) throws Exception {
        UnMarshaler unMarshaler = new IMCUnMarshaler(System.in);
        Marshaler marshaler = new IMCMarshaler(System.out);
        String readline;
        do {
            readline = unMarshaler.readStringLine();
            marshaler.writeStringLine("input: " + readline);
        } while (true);
    }
}
