class BackupThread extends Thread {
    public String getFileName() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.getStart());
        String fileName = this.getFilePattern();
        fileName = fileName.replaceAll("%y", addZero(cal.get(Calendar.YEAR)));
        fileName = fileName.replaceAll("%m", addZero((cal.get(Calendar.MONTH) + 1)));
        fileName = fileName.replaceAll("%d", addZero(cal.get(Calendar.DATE)));
        fileName = fileName.replaceAll("%h", addZero(cal.get(Calendar.HOUR_OF_DAY)));
        fileName = fileName.replaceAll("%M", addZero(cal.get(Calendar.MINUTE)));
        String dayOfWeek = "";
        try {
            dayOfWeek = (String) DataStore.getInstance().dayName.get(new Integer(cal.get(Calendar.DAY_OF_WEEK)));
        } catch (Exception e) {
        }
        fileName = fileName.replaceAll("%D", dayOfWeek);
        if (this.getName().length() > 0) fileName = fileName.replaceAll("%n", checkFileName(this.getName())); else fileName = fileName.replaceAll("%n", "");
        if (createdFrom != null) {
            fileName = fileName.replaceAll("%s", checkFileName(createdFrom.getSubName()));
            String allCats = "";
            for (int x = 0; x < createdFrom.getCategory().size(); x++) {
                allCats += createdFrom.getCategory().get(x);
                if (x < createdFrom.getCategory().size() - 1) allCats += "-";
            }
            fileName = fileName.replaceAll("%t", checkFileName(allCats));
        } else {
            fileName = fileName.replaceAll("%s", "");
            fileName = fileName.replaceAll("%t", "");
        }
        fileName = fileName.replaceAll("%c", checkFileName(this.getChannel()));
        fileName = fileName.replaceAll("(\\ )+", " ");
        String finalName = fileName.trim();
        return finalName;
    }
}
