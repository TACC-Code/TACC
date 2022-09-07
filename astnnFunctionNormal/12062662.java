class BackupThread extends Thread {
    private void addMembership(Scanner mainInput, PrintStream output) {
        output.println(mainInput.nextLine());
        String line = mainInput.nextLine();
        int lines = Integer.parseInt(new StringTokenizer(line).nextToken());
        output.println(line);
        for (int i = 0; i < lines; i++) output.println(mainInput.nextLine());
    }
}
