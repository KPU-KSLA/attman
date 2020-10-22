package com.dps0340.attman

import android.content.Context
import android.graphics.*
import android.view.View

internal class MyView(context: Context?) : View(context) {
    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.textSize = 60f
        paint.color = Color.CYAN //첫줄 제목 색깔 청녹색
        val t = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        paint.typeface = t
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("코로나바이러스 감염증 대응", 540f, 600f, paint) //첫화면 제목 첫줄
        paint.color = Color.BLUE //둘째줄 제목 색깔 파란색
        canvas.drawText("자기지킴이 출석체크 시스템", 540f, 680f, paint) //첫화면 제목 둘째줄
        val b = BitmapFactory.decodeResource(resources, R.drawable.maskicon) //첫화면 마스크아이콘 삽입
        canvas.drawBitmap(b, 420f, 220f, null)
        val b2 = BitmapFactory.decodeResource(resources, R.drawable.kpu_logo) //첫화면 한국산업기술대로고 삽입
        val sb2 = Bitmap.createScaledBitmap(b2, 500, 200, false)
        canvas.drawBitmap(sb2, 280f, 1200f, null)
    }
}