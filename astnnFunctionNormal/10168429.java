class BackupThread extends Thread {
    private boolean updateFavourite() {
        boolean setField = false;
        calcTxtName();
        favourite.setName(txtName.getText());
        if (cmbTitle.getSelectedItem().equals(Application.getInstance().getLocalizedMessage("exactly"))) {
            favourite.setTitleRegex(null);
            favourite.setTitleContains(null);
            String tmp = txtTitle.getText();
            if (tmp.equals(StringHelper.EMPTY_STRING)) {
                favourite.setTitleString(null);
            } else {
                favourite.setTitleString(tmp);
                setField = true;
            }
        } else if (cmbTitle.getSelectedItem().equals(Application.getInstance().getLocalizedMessage("contains"))) {
            favourite.setTitleString(null);
            favourite.setTitleRegex(null);
            String tmp = txtTitle.getText();
            if (tmp.equals(StringHelper.EMPTY_STRING)) {
                favourite.setTitleContains(null);
            } else {
                favourite.setTitleContains(tmp);
                setField = true;
            }
        } else {
            favourite.setTitleString(null);
            favourite.setTitleContains(null);
            String tmp = txtTitle.getText();
            if (tmp.equals(StringHelper.EMPTY_STRING)) {
                favourite.setTitleRegex(null);
            } else {
                favourite.setTitleRegex(tmp);
                setField = true;
            }
        }
        Object sel = cmbChannel.getSelectedItem();
        if (sel instanceof TVChannelsSet.Channel) {
            favourite.setChannelID(((TVChannelsSet.Channel) sel).getChannelID());
            setField = true;
        } else {
            favourite.setChannelID(null);
        }
        String tmp = txtAfter.getText();
        if (!tmp.equals(StringHelper.EMPTY_STRING) && (tmp.length() == 5) && (tmp.charAt(2) == ':')) {
            favourite.setAfterTime(new Time(tmp));
            setField = true;
        } else {
            favourite.setAfterTime(new Time());
        }
        tmp = txtBefore.getText();
        if (!tmp.equals(StringHelper.EMPTY_STRING) && (tmp.length() == 5) && (tmp.charAt(2) == ':')) {
            favourite.setBeforeTime(new Time(tmp));
            setField = true;
        } else {
            favourite.setBeforeTime(new Time());
        }
        tmp = (String) cmbDayOfWeek.getSelectedItem();
        if (!tmp.equals(StringHelper.EMPTY_STRING)) {
            Calendar cal = GregorianCalendar.getInstance();
            try {
                cal.setTime(dayOfWeekFormat.parse(tmp));
                favourite.setDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));
                setField = true;
            } catch (java.text.ParseException ex) {
                Application.getInstance().getLogger().log(Level.WARNING, "Error on parse day of week", ex);
                favourite.setDayOfWeek(-1);
            }
        } else {
            favourite.setDayOfWeek(-1);
        }
        setChanged();
        return setField;
    }
}
