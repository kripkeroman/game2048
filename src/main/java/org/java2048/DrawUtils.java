package org.java2048;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawUtils
{
    private DrawUtils() {}

    public static int getMessageWitgh(String message, Font font, Graphics2D g)
    {
        g.setFont(font);
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g);
        return (int) bounds.getWidth();
    }

    public static int getMessageHeitgh(String message, Font font, Graphics2D g)
    {
        g.setFont(font);
        if(message.length() == 0) return 0;
        TextLayout layout = new TextLayout(message, font, g.getFontRenderContext());
        return (int) layout.getBounds().getHeight();
    }
}
