package com.unrealdinnerbone.weathergate.client.screen;

import dev.ftb.mods.ftblibrary.client.gui.widget.Panel;
import dev.ftb.mods.ftblibrary.client.gui.widget.TextBox;
import net.minecraft.util.Mth;

import java.util.function.Predicate;

public class FloatTextBox extends TextBox {

    private static final Predicate<String> IS_NUMBER = (s) -> s.matches("^-?[0-9]*(\\.[0-9]*)?$");
    private float min = Float.NEGATIVE_INFINITY;
    private float max = Float.POSITIVE_INFINITY;

    public FloatTextBox(Panel panel) {
        super(panel);
        this.setFilter(IS_NUMBER);
        this.setStrictValidity(true);
    }

    public float getFloatValue() {
        String text = this.getText();
        if (!text.isEmpty() && !text.equals("-")) {
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException var3) {
                return Mth.clamp(0, this.min, this.max);
            }
        } else {
            return Mth.clamp(0, this.min, this.max);
        }
    }

    public void setMin(float min) {
        this.min = min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMinMax(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public boolean mouseScrolled(double scroll) {
        if (this.allowInput()) {
            this.setAmount(this.getFloatValue() + (float)scroll);
            return true;
        } else {
            return false;
        }
    }

    public void setAmount(float amount) {
        this.setText(String.valueOf(amount));
    }

    public void onTextChanged() {
        this.ensureValue();
    }

    public void ensureValue() {
        float amount = this.getFloatValue();
        if (amount < this.min) {
            this.setAmount(this.min);
        } else if (amount > this.max) {
            this.setAmount(this.max);
        }

    }
}
