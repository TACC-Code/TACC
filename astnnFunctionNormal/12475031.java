class BackupThread extends Thread {
    public void itemSelected(ItemIF sItem) {
        if (sItem != null) {
            log.info("sel item: Ch:" + sItem.getChannel().getTitle() + " Title:" + sItem.getTitle() + " subj:" + sItem.getSubject() + " date: " + sItem.getDate() + "\n     creatr: " + sItem.getCreator() + " found:" + sItem.getFound() + " ID:" + sItem.getId() + " \ndesc:" + sItem.getDescription() + "\n");
            itemDetailView.update(sItem.getTitle(), sItem.getCreator(), sItem.getDate(), sItem.getSubject(), sItem.getDescription(), sItem.getFound(), sItem.getLink());
        }
    }
}
