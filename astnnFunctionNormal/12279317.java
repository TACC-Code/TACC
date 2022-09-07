class BackupThread extends Thread {
    public static void convert(Scanner input, Locale locale, PrintStream output) throws IOException {
        while (input.hasNextLine()) {
            output.println(input.nextLine().toLowerCase(locale));
        }
    }
}
