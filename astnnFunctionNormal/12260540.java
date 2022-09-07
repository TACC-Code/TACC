class BackupThread extends Thread {
    private void deleteFriend(YahooUser yu, String gr) {
        int idx, j;
        for (idx = 0; idx < groups.length; idx++) if (groups[idx].getName().equalsIgnoreCase(gr)) break;
        if (idx >= groups.length) return;
        j = groups[idx].getIndexOfFriend(yu.getId());
        if (j < 0) return;
        groups[idx].removeUserAt(j);
        yu.adjustGroupCount(-1);
        if (groups[idx].isEmpty()) {
            YahooGroup[] arr = new YahooGroup[groups.length - 1];
            for (j = 0; j < idx; j++) arr[j] = groups[j];
            for (j = idx; j < arr.length; j++) arr[j] = groups[j + 1];
            groups = arr;
        }
    }
}
