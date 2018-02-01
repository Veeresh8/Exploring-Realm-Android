package veeresh.a3c.realm.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import veeresh.a3c.realm.R;
import veeresh.a3c.realm.application.RealmApplication;


public class MyCustomTextView extends TextView {

    private int typefaceType;

    public MyCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCustomTextView,
                0, 0);
        try {
            typefaceType = array.getInteger(R.styleable.MyCustomTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(RealmApplication.getInstance().getTypeFace(typefaceType));
        }
    }
}
