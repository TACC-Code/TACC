class BackupThread extends Thread {
    public static SpeciesTree create(String url) throws IOException {
        SpeciesTree tree = new SpeciesTree();
        tree.setUrl(url);
        System.out.println("Fetching URL:  " + url);
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        String toParse = null;
        while (in.ready()) {
            String line = in.readLine();
            if (line == null) break;
            line = line.trim();
            String TREE = "TREE:";
            if (!line.startsWith(TREE)) continue;
            int end = line.indexOf(';');
            if (end < 0) end = line.length();
            toParse = line.substring(TREE.length(), end).trim();
        }
        System.out.print("Parsing... ");
        parse(tree, toParse);
        return tree;
    }
}
