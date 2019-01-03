# ScanView

一个类似雷达扫描的动画


![效果](https://github.com/walker0402/ScanView/blob/master/show/效果.gif)


效果图下面的阴影是录制gif时窗口的阴影，不要在意。

分析下这个动画，底图是一个圆形的图片，图片上层盖了一个贴边的内圈圆环，然后中心有一个小圆再不断的绕圈扫描，小圆与圆环之间还有一定的间隔。

圆环的实现是这样的，以一个大圆跟一个小圆相交，裁去相交的那部分，剩下的就是我们要的圆环

		 int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
	//        //绘制Dst圆
	        canvas.drawBitmap(xfermod_dstBitmap, 0, 0, mPaint);
	//        //设置Xfermode
	        mPaint.setXfermode(porterDuffXfermode);
	          //绘制Src圆
	        canvas.drawBitmap(xfermod_srcBitmap, 0, 0, mPaint);
	        //绘制完成需要置null
	        mPaint.setXfermode(null);
	        //缓冲完毕复原
	        canvas.restoreToCount(saved);

要注意的是，这种实现方式需要使用离屏缓冲，否则无法达到预期效果，也就是这两行代码

	 int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
	 canvas.restoreToCount(saved);

另外在实现中发现，只有在两个bitmap相处理的时候，才有

![proterDuff.mode](https://github.com/walker0402/ScanView/blob/master/show/PorterDuff.Mode.jpg)

这个实现的效果。

所以在处理圆环的时候，我是创建了两个bitmap的。
假如是直接画两个同心圆，


   			 canvas.drawCircle(centerX, centerY, middleRadius, mPaint);
	        mPaint.setXfermode(porterDuffXfermode);
	        mPaint.setColor(Color.parseColor("#33000000"));
	        canvas.drawCircle(centerX, centerY, outerRadius, mPaint);Circle(centerX, centerY, outerRadius, mPaint);
实际是这样的。

![实例](https://github.com/walker0402/ScanView/blob/master/show/实例.png)


最后是扫描的动画效果，比较简单，不停的加大扇形的绘制角度就行了。