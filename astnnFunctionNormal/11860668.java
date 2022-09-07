class BackupThread extends Thread {
    public void onEvent(Event event) throws Exception {
        if (Events.ON_CLICK.equals(event.getName()) && readwrite) {
            WImageDialog vid = new WImageDialog(m_mImage);
            if (!vid.isCancel()) {
                int AD_Image_ID = vid.getAD_Image_ID();
                Object oldValue = getValue();
                Integer newValue = null;
                if (AD_Image_ID != 0) newValue = new Integer(AD_Image_ID);
                m_mImage = null;
                setValue(newValue);
                ValueChangeEvent vce = new ValueChangeEvent(this, gridField.getColumnName(), oldValue, newValue);
                fireValueChange(vce);
            }
        }
    }
}
