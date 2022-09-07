class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        String target = "d://prefix.csv";
        URL google = new URL("http://prefix.cc/popular/all.file.csv");
        ReadableByteChannel rbc = Channels.newChannel(google.openStream());
        File file = new File(target);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos.close();
        File tmpFile = new File(target + ".tmp");
        File inFile = new File(target);
        PrintWriter pw = new PrintWriter(new FileWriter(tmpFile));
        BufferedReader br = new BufferedReader(new FileReader(inFile));
        String line = null;
        Set<String> values = new HashSet<String>();
        while ((line = br.readLine()) != null) {
            String[] entry = line.split(",");
            if (entry.length == 2) {
                String key = entry[0];
                String value = entry[1];
                value = value.substring(1);
                value = value.substring(0, value.length() - 1);
                if (!value.trim().isEmpty() && !values.contains(value)) {
                    values.add(value);
                    pw.println(entry[0] + "," + value);
                    pw.flush();
                }
            }
        }
        pw.close();
        br.close();
        inFile.delete();
        tmpFile.renameTo(inFile);
    }
}
