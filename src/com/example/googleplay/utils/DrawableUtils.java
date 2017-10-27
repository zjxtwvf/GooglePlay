package com.example.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {

	//��ȡһ��shape����
	public static GradientDrawable getGradientDrawable(int color, int radius) {
		// xml�ж����shape��ǩ ��Ӧ����
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE);// ����
		shape.setCornerRadius(radius);// Բ�ǰ뾶
		shape.setColor(color);// ��ɫ

		return shape;
	}

	//��ȡ״̬ѡ����
	public static StateListDrawable getSelector(Drawable normal, Drawable press) {
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[] { android.R.attr.state_pressed }, press);// ����ͼƬ
		selector.addState(new int[] {}, normal);// Ĭ��ͼƬ

		return selector;
	}
	
	//��ȡ״̬ѡ����
	public static StateListDrawable getSelector(int normal, int press, int radius) {
		GradientDrawable bgNormal = getGradientDrawable(normal, radius);
		GradientDrawable bgPress = getGradientDrawable(press, radius);
		StateListDrawable selector = getSelector(bgNormal, bgPress);
		return selector;
	}


}