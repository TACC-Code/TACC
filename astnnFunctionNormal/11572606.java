class BackupThread extends Thread {
    @Override
    public void ReadText() {
        try {
            URL url = new URL(pagePath);
            InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream());
            BufferedReader br = new BufferedReader(isr);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                txt.add(line);
            }
            br.close();
            return;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
