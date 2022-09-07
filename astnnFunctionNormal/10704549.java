class BackupThread extends Thread {
    private void createViewer() {
        String[] names = deckList.getSelectedItems();
        if (names.length == 0) {
            setMessage("No objectives selected.");
            return;
        }
        setMessage("");
        setMessage("creating deck request");
        String deckUrl = createDeckUrl(names);
        boolean error = true;
        try {
            setMessage("fetching deck");
            URL url = new URL(deckUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream in = url.openStream();
            SimpleXmlDeckBuilder builder = new SimpleXmlDeckBuilder();
            Deck deck = builder.build(in);
            DeckViewer viewer = new DeckViewer(deck);
            Insets insets = getInsets();
            viewer.setSize(viewer.getSize().width + insets.left + insets.right, viewer.getSize().height + insets.top + insets.bottom);
            viewer.show();
            viewer.setVisible(true);
            setMessage("viewer loaded");
            error = false;
        } catch (MalformedURLException e) {
            setMessage("deck not loaded:invalid url:" + deckUrl);
        } catch (IOException e) {
            setMessage("deck not loaded:" + e);
        } catch (Exception e) {
            setMessage("deck not loaded:" + e);
            e.printStackTrace();
        } finally {
            if (error) {
                String message = "Deck not retreived.\n" + "Please log in.\n" + "If the problem persists, please " + "contact flash@code316.com and include any relevant information" + "screenshots, username, etc.";
                setMessage(message);
            }
        }
    }
}
