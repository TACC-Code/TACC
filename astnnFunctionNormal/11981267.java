class BackupThread extends Thread {
    private void listEntries(ContactsExampleParameters parameters) throws IOException, ServiceException {
        if (parameters.isGroupFeed()) {
            ContactGroupFeed groupFeed = service.getFeed(feedUrl, ContactGroupFeed.class);
            System.err.println(groupFeed.getTitle().getPlainText());
            for (ContactGroupEntry entry : groupFeed.getEntries()) {
                printGroup(entry);
            }
            System.err.println("Total: " + groupFeed.getEntries().size() + " groups found");
        } else {
            ContactFeed resultFeed = service.getFeed(feedUrl, ContactFeed.class);
            System.err.println(resultFeed.getTitle().getPlainText());
            for (ContactEntry entry : resultFeed.getEntries()) {
                printContact(entry);
                Link photoLink = entry.getLink("http://schemas.google.com/contacts/2008/rel#photo", "image/*");
                if (photoLink.getEtag() != null) {
                    Service.GDataRequest request = service.createLinkQueryRequest(photoLink);
                    request.execute();
                    InputStream in = request.getResponseStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    RandomAccessFile file = new RandomAccessFile("/tmp/" + entry.getSelfLink().getHref().substring(entry.getSelfLink().getHref().lastIndexOf('/') + 1), "rw");
                    byte[] buffer = new byte[4096];
                    for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) {
                    }
                    file.write(out.toByteArray());
                    file.close();
                    in.close();
                    request.end();
                }
            }
            System.err.println("Total: " + resultFeed.getEntries().size() + " entries found");
        }
    }
}
