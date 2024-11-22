package other.template.res.layout

fun defaultLayoutKt() = """
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</androidx.constraintlayout.widget.ConstraintLayout>
""".trimIndent()

fun defaultListLayout(
    itemLayoutName: String
) = """
<?xml version="1.0" encoding="utf-8"?>
<com.scwang.smart.refresh.layout.SmartRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@id/root_refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <androidx.recyclerview.widget.RecyclerView
        tools:listitem="@layout/${itemLayoutName}"
        android:id="@id/root_rv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</com.scwang.smart.refresh.layout.SmartRefreshLayout>
""".trimIndent()

fun defaultListItemLayout() = """
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

</androidx.constraintlayout.widget.ConstraintLayout>
""".trimIndent()