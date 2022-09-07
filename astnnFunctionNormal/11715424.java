class BackupThread extends Thread {
    public Content[] contentStatic(Integer[] ids) throws TemplateNotFoundException, TemplateParseException, GeneratedZeroStaticPageException, StaticPageNotOpenException, ContentNotCheckedException {
        int count = 0;
        List<Content> list = new ArrayList<Content>();
        for (int i = 0, len = ids.length; i < len; i++) {
            Content content = findById(ids[i]);
            try {
                if (!content.getChannel().getStaticContent()) {
                    throw new StaticPageNotOpenException("content.staticNotOpen", count, content.getTitle());
                }
                if (!content.isChecked()) {
                    throw new ContentNotCheckedException("content.notChecked", count, content.getTitle());
                }
                if (staticPageSvc.content(content)) {
                    list.add(content);
                    count++;
                }
            } catch (IOException e) {
                throw new TemplateNotFoundException("content.tplContentNotFound", count, content.getTitle());
            } catch (TemplateException e) {
                throw new TemplateParseException("content.tplContentException", count, content.getTitle());
            }
        }
        if (count == 0) {
            throw new GeneratedZeroStaticPageException("content.staticGenerated");
        }
        Content[] beans = new Content[count];
        return list.toArray(beans);
    }
}
