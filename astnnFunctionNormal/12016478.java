class BackupThread extends Thread {
    private void removeFromFile(String name) throws IOException {
        List<String> names = new Vector<String>();
        Scanner scanner = new Scanner(file);
        String line;
        try {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine().trim().toLowerCase();
                if (!line.isEmpty()) names.add(line);
            }
        } finally {
            scanner.close();
        }
        names.remove(name.toLowerCase());
        FileOutputStream fs = new FileOutputStream(file);
        fs.getChannel().truncate(0);
        fs.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Iterator<String> i = names.iterator();
        try {
            while (i.hasNext()) {
                writer.write(i.next() + "\n");
            }
        } finally {
            writer.close();
        }
    }
}
