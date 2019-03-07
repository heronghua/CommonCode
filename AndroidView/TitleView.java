package cn.make1.vangelis.makeonec.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.make1.vangelis.makeonec.R;


public class TitleView extends RelativeLayout {
	
	private final LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
	
	public static final int DEFAULT_TITLE_COLOR = Color.BLACK;
	
	private float titleTextSize,leftTextSize,rightTextSize;
	
	private int titleTextColor,leftTextColor,rightTextColor;
	
	private String leftText="",rightText="",titleName;
	
	private Drawable leftDrawable,rightDrawable;

	private String leftClick,rightClick;
	
	private View left ,center,right;
	
	private boolean leftEnable,rightEnable;
	
	private TextView rightTextView;
	
	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.TitleView);
		
		this.titleTextSize = a.getDimensionPixelSize(R.styleable.TitleView_titleTextSize,16);
		this.leftTextSize = a.getDimensionPixelSize(R.styleable.TitleView_leftTextSize, 16);
		this.rightTextSize = a.getDimensionPixelSize(R.styleable.TitleView_rightTextSize, 16);
		
		this.titleTextColor = a.getColor(R.styleable.TitleView_titleTextColor, DEFAULT_TITLE_COLOR);
		this.leftTextColor = a.getColor(R.styleable.TitleView_leftTextColor, DEFAULT_TITLE_COLOR);
		this.rightTextColor = a.getColor(R.styleable.TitleView_rightTextColor, DEFAULT_TITLE_COLOR);
		this.titleName = a.getString(R.styleable.TitleView_titleName);
		
		this.leftEnable = a.getBoolean(R.styleable.TitleView_leftEnable, true);
		this.rightEnable = a.getBoolean(R.styleable.TitleView_rightEnable, true);
		
        this.leftClick = a.getString(R.styleable.TitleView_leftClick);
        this.rightClick = a.getString(R.styleable.TitleView_rightClick);
         
		
		try {
			this.leftDrawable = a.getDrawable(R.styleable.TitleView_leftElement);
		} catch (Exception e) {
			this.leftText = a.getString(R.styleable.TitleView_leftElement);
		}
		
		try {
			this.rightDrawable = a.getDrawable(R.styleable.TitleView_rightElement);
		} catch (Exception e) {
			this.rightText = a.getString(R.styleable.TitleView_rightElement);
		}
		a.recycle();
		
		addViews();
		resetPadding();
		setListners();
		
		setLeftEnable(leftEnable);
		setRightEnable(rightEnable);
	}

	private void setListners() {
		if (leftClick != null&&left!=null) {
			left.setOnClickListener(new DeclaredOnClickListener(this, leftClick));
        }
		
		if (rightClick != null&&right!=null) {
			right.setOnClickListener(new DeclaredOnClickListener(this, rightClick));
        }
		
	}
	
	public void setLeftEnable(boolean leftEnable) {
		if (left!=null) {
			this.leftEnable = leftEnable;
			left.setEnabled(leftEnable);
		}
	}
	
	public void setRightEnable(boolean rightEnable) {
		if (right!=null) {
			this.rightEnable = rightEnable;
			right.setEnabled(rightEnable);
		}
	}

	private void resetPadding() {
		if (left!=null) {
			left.setPadding(getPaddingLeft(), 0, getPaddingLeft(), 0);
		}
		
		if (right!=null) {
			right.setPadding(getPaddingRight(), 0, getPaddingRight(), 0);
		}
		
		this.setPadding(0, 0, 0, 0);
		
	}
	
	public void setLeftVisible(int visibility){
		if (left!=null) {
			this.left.setVisibility(visibility);
		}		
	}
	
	public void setRightVisible(int visibility){
		if (right!=null) {
			this.right.setVisibility(visibility);
		}		
	}

	public void setRightText(String text){
		if(rightTextView != null){
			rightTextView.setText(text);
		}
	}
	
	public void setRightText(int resId){
		if(rightTextView != null){
			rightTextView.setText(resId);
		}
	}
	
	private void addViews() {
		if (leftDrawable!=null) {
			ImageView imageView = new ImageView(getContext());
			imageView.setImageDrawable(leftDrawable);
			this.left = imageView;
			LayoutParams layoutParams=new LayoutParams(138, LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			this.addView(imageView,layoutParams);
		}
		
		if (!TextUtils.isEmpty(leftText)) {
			TextView leftTextView = new TextView(getContext());
			leftTextView.setGravity(Gravity.CENTER);
			leftTextView.setText(leftText);
			leftTextView.setTextColor(leftTextColor);
			leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,leftTextSize);
			this.left = leftTextView;
			this.addView(leftTextView,lp);
		}
		
		TextView titleTextView = new TextView(getContext());
		titleTextView.setText(titleName);
		titleTextView.setTextColor(titleTextColor);
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleTextSize);
		titleTextView.setGravity(Gravity.CENTER);
		LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(titleTextView,lp);
		this.center = titleTextView;
		
		LayoutParams lp0=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		lp0.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		if (rightDrawable!=null) {
			ImageView imageView = new ImageView(getContext());
			imageView.setImageDrawable(rightDrawable);
			this.addView(imageView,lp0);
			this.right=imageView;
		}
		
		if (!TextUtils.isEmpty(rightText)) {
		    rightTextView = new TextView(getContext());
			rightTextView.setGravity(Gravity.CENTER);
			rightTextView.setText(rightText);
			rightTextView.setTextColor(rightTextColor);
			rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,rightTextSize);
			this.addView(rightTextView,lp0);
			this.right = rightTextView;
		}
		
	}
	
    private static class DeclaredOnClickListener implements OnClickListener {
        private final View mHostView;
        private final String mMethodName;

        private Method mMethod;

        public DeclaredOnClickListener(View hostView, String methodName) {
            mHostView = hostView;
            mMethodName = methodName;
        }

        @Override
        public void onClick(View v) {
            if (mMethod == null) {
                mMethod = resolveMethod(mHostView.getContext(), mMethodName);
            }

            try {
                mMethod.invoke(mHostView.getContext(), v);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(
                        "Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(
                        "Could not execute method for android:onClick", e);
            }
        }

        private Method resolveMethod(Context context, String name) {
            while (context != null) {
                try {
                    if (!context.isRestricted()) {
                        return context.getClass().getMethod(name, View.class);
                    }
                } catch (NoSuchMethodException e) {
                    // Failed to find method, keep searching up the hierarchy.
                }

                if (context instanceof ContextWrapper) {
                    context = ((ContextWrapper) context).getBaseContext();
                } else {
                    // Can't search up the hierarchy, null out and fail.
                    context = null;
                }
            }

            final int id = mHostView.getId();
            final String idText = id == NO_ID ? "" : " with id '"
                    + mHostView.getContext().getResources().getResourceEntryName(id) + "'";
            throw new IllegalStateException("Could not find method " + mMethodName
                    + "(View) in a parent or ancestor Context for android:onClick "
                    + "attribute defined on view " + mHostView.getClass() + idText);
        }
    }

	public void setTitle(String text) {
		((TextView)center).setText(text);
	}

	public void setTitle(int resId) {
		setTitle(getContext().getResources().getString(resId));
	}
	
	public interface OnRightElementClickListner{
		void onRightElementClick(View view);
	}
	
	public interface OnLeftElementClickListner{
		void onLeftElementClick(View view);
	}

	public void setOnRightIconClickListner(final OnRightElementClickListner onRightElementClickListner) {
		right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRightElementClickListner.onRightElementClick(v);
			}
		});
	}
	
	public void setOnLeftIconClickListner(final OnLeftElementClickListner onLeftElementClickListner) {
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onLeftElementClickListner.onLeftElementClick(v);
			}
		});
	}
	
}
