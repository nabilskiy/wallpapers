package wallgram.hd.wallpapers.presentation.subscribe;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class LinearGradientForegroundSpan extends CharacterStyle implements UpdateAppearance {
    private int startColor;
    private int endColor;
    private int lineHeight;

    public LinearGradientForegroundSpan(int startColor, int endColor, int lineHeight) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.lineHeight = lineHeight;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setShader(new LinearGradient(0, 0, lineHeight, 0,
                startColor, endColor, Shader.TileMode.CLAMP));
    }
}