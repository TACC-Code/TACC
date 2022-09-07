class BackupThread extends Thread {
        public Object getValueAt(int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount()) return "";
            Video video = (Video) mShows.get(nRow);
            switch(nCol) {
                case 0:
                    return video.getTitle() != null ? video.getTitle() : "";
                case 1:
                    return video.getEpisodeTitle() != null ? video.getEpisodeTitle() : "";
                case 2:
                    mCalendar.setTime(video.getDateRecorded());
                    mCalendar.set(GregorianCalendar.MINUTE, (mCalendar.get(GregorianCalendar.MINUTE) * 60 + mCalendar.get(GregorianCalendar.SECOND) + 30) / 60);
                    mCalendar.set(GregorianCalendar.SECOND, 0);
                    return mDateFormat.format(mCalendar.getTime());
                case 3:
                    int duration = Math.round(video.getDuration() / 1000 / 60 + 0.5f);
                    mCalendar.setTime(new Date(Math.round((video.getDuration() / 1000 / 60 + 0.5f) / 10) * 10));
                    mCalendar.set(GregorianCalendar.HOUR_OF_DAY, duration / 60);
                    mCalendar.set(GregorianCalendar.MINUTE, duration % 60);
                    mCalendar.set(GregorianCalendar.SECOND, 0);
                    return mTimeFormat.format(mCalendar.getTime());
                case 4:
                    return mNumberFormat.format(video.getSize() / (1024 * 1024)) + " MB";
                case 5:
                    return video.getStatusString();
                case 6:
                    return (video.getDescription() != null && video.getDescription().length() != 0) ? video.getDescription() : " ";
                case 7:
                    String txtChannel = "";
                    String txtStation = "";
                    if (video.getChannel() != null) txtChannel = video.getChannel();
                    if (video.getStation() != null) txtStation = video.getStation();
                    return txtChannel + " " + txtStation;
                case 8:
                    return (video.getRating() != null && video.getRating().length() != 0) ? video.getRating() : "No rating";
                case 9:
                    if (video.getHighDefinition().equals("Yes")) return "[HD]"; else if (video.getRecordingQuality() == null) return "DIGITAL"; else if (video.getRecordingQuality().length() == 0) return "UNKNOWN"; else if (video.getRecordingQuality().equalsIgnoreCase("good")) return "BASIC"; else return video.getRecordingQuality();
            }
            return " ";
        }
}
