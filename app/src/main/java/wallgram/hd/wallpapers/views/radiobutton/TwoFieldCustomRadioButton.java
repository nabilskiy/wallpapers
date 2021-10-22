package wallgram.hd.wallpapers.views.radiobutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import wallgram.hd.wallpapers.R;

public final class TwoFieldCustomRadioButton extends BaseCustomRadioButton {

    private TextView priceTextView;
    private TextView periodTextView;
    private TextView discountTextView;

    private String price;
    private String discount;
    private String period;

    public TwoFieldCustomRadioButton(Context context) {
        super(context, R.layout.custom_button_two_field, R.styleable.CustomRadioButtonOneField);
    }

    public TwoFieldCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.custom_button_two_field,
                R.styleable.CustomRadioButtonOneField);
    }

    public TwoFieldCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.custom_button_two_field,
                R.styleable.CustomRadioButtonOneField);
    }

    @Override
    protected void bindViews() {
        priceTextView = findViewById(R.id.price_text);
        periodTextView = findViewById(R.id.period_text);
        discountTextView = findViewById(R.id.discount_text);
    }

    @Override
    protected void initAttributes() {
        initPrice(R.styleable.CustomRadioButtonOneField_price_field);
        initPeriod(R.styleable.CustomRadioButtonOneField_period_field);
        initDiscount(R.styleable.CustomRadioButtonOneField_discount_field);
    }

    private void initPrice(int index) {
        if(typedArrayHasValue(index)) {
            price = a.getString(index);
        }
    }

    private void initDiscount(int index) {
        if(typedArrayHasValue(index)) {
            discount = a.getString(index);
        }
    }

    private void initPeriod(int index) {
        if(typedArrayHasValue(index)) {
            period = a.getString(index);
        }
    }

    private boolean typedArrayHasValue(int index) {
        return a.hasValue(index);
    }

    public void setPrice(String price){
        this.price = price;
        populateViews();
    }

    @Override
    public void setCurrent(boolean isCurrent){
        discountTextView.setVisibility(isCurrent ? VISIBLE : GONE);
    }

    @Override
    protected void populateViews() {
        priceTextView.setText(price);
        periodTextView.setText(period);
        discountTextView.setText(discount);
    }

    @Override
    public String getPrice(){
        return price;
    }

    @Override
    public String getDuration(){
        if(getId() == R.id.month_sub){
            return "1" + getContext().getString(R.string.month).replace("/ ", "");
        }
        else if(getId() == R.id.year_sub){
            return "1" + getContext().getString(R.string.year).replace("/ ", "");
        }
        else return "0";
    }
}

