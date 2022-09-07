class BackupThread extends Thread {
    public final java.awt.Font getAWTFont(HUD hud) {
        boolean useScaling = useFontScaling && hud.hasCustomResolution();
        if (useScaling) {
            if ((this.awtFont == null) || (this.lastHUDHeight != (int) hud.getResY())) {
                if (url == null) {
                    this.awtFont = new java.awt.Font(name, style.getAWTStyle(), Math.round(size * hud.getHeight() / hud.getResY()));
                } else if (this.awtFont == null) {
                    try {
                        this.awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, url.openStream()).deriveFont(Math.round(size * hud.getHeight() / hud.getResY())).deriveFont(style.getAWTStyle(), size);
                    } catch (Throwable t) {
                        throw new Error(t);
                    }
                } else {
                    this.awtFont = this.awtFont.deriveFont(style.getAWTStyle(), size);
                }
                this.metrics = null;
                this.lastHUDHeight = (int) hud.getResY();
            }
        } else {
            if (awtFont == null) {
                if (url == null) {
                    this.awtFont = new java.awt.Font(name, style.getAWTStyle(), size);
                } else {
                    try {
                        this.awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, url.openStream()).deriveFont(style.getAWTStyle(), size);
                    } catch (Throwable t) {
                        throw new Error(t);
                    }
                }
                this.metrics = null;
            }
        }
        return (awtFont);
    }
}
