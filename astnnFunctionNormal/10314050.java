class BackupThread extends Thread {
    public static void inspect(PrintWriter writer, String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        writer.println(filename);
        writer.print("Number of pages: ");
        writer.println(reader.getNumberOfPages());
        Rectangle mediabox = reader.getPageSize(1);
        writer.print("Size of page 1: [");
        writer.print(mediabox.getLeft());
        writer.print(',');
        writer.print(mediabox.getBottom());
        writer.print(',');
        writer.print(mediabox.getRight());
        writer.print(',');
        writer.print(mediabox.getTop());
        writer.println("]");
        writer.print("Rotation of page 1: ");
        writer.println(reader.getPageRotation(1));
        writer.print("Page size with rotation of page 1: ");
        writer.println(reader.getPageSizeWithRotation(1));
        writer.print("Is rebuilt? ");
        writer.println(reader.isRebuilt());
        writer.print("Is encrypted? ");
        writer.println(reader.isEncrypted());
        writer.println();
        writer.flush();
    }
}
