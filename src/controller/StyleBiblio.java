package controller;
import java.awt.*;


public class StyleBiblio {
    private Color primaryColor = new Color(65, 105, 225); // Bleu royal
    private Color secondaryColor = new Color(240, 248, 255); // Alice Blue
    private Color accentColor = new Color(255, 99, 71); // Tomato
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);

    public Color getAccentColor() {
        return accentColor;
    }
    public Font getNormalFont() {
        return normalFont;
    }
    public Color getPrimaryColor() {
        return primaryColor;
    }
    public Color getSecondaryColor() {
        return secondaryColor;
    }
    public Font getSmallFont() {
        return smallFont;
    }
    public Font getTitleFont() {
        return titleFont;
    }
}
