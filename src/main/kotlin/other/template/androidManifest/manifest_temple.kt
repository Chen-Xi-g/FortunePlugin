package other.template.androidManifest

fun manifestTemplateXml(
    packageName: String,
    activityClass: String
) = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application>
	    <activity 
            android:screenOrientation="portrait"
            android:name="$packageName${activityClass}" />
    </application>
</manifest>
"""