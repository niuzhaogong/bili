package cn.zndroid.bili.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zndroid.bili.R;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class CustomEmptyView extends FrameLayout {

    private ImageView mEmptyImg;
    private TextView mEmptyText;

    public CustomEmptyView(@NonNull Context context) {
        this(context,null);
    }
    public CustomEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
          this(context, attrs,0);
    }
    public CustomEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, this);
        mEmptyImg = (ImageView) view.findViewById(R.id.empty_img);
        mEmptyText = (TextView) view.findViewById(R.id.empty_text);
    }
    public void setEmptyImage(int imgRes) {
        mEmptyImg.setImageResource(imgRes);
    }

    public void setEmptyText(String text) {
        mEmptyText.setText(text);
    }
}
