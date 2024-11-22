package other.template.res.layout

fun viewPager2ActivityLayout() = """
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <View
        android:id="@+id/v_padding"
        android:layout_width="1px"
        android:layout_height="1px"/>
    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/vp2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
""".trimIndent()