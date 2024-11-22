package other.notify

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.NotificationsManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File

object NotifyAction {

    const val NOTIFY_SUCCESS = "Custom MVVM Output File Success"

    /**
     * 通知文件生成成功, 可操作打开文件
     *
     * @param type 类型(1: 生成Activity, 2: 生成Fragment, 3: 生成ViewPager2)
     */
    fun notifyFileSuccess(
        type: Int,
        files: List<File>
    ) {
        val project = ProjectManager.getInstance().openProjects.firstOrNull()
        project ?: return
        val title = when (type) {
            1 -> "创建 MVVM Activity 成功"
            2 -> "创建 MVVM Fragment 成功"
            3 -> "创建 MVVM ViewPager2 成功"
            else -> "创建成功"
        }
        val content = "叮～ ${files.size} 个 MVVM 文件已成功生成！祝开发顺利！"
        // 检查是否存在重复通知
        val existingNotifications = NotificationsManager.getNotificationsManager()
            .getNotificationsOfType(Notification::class.java, project)
        if (existingNotifications.any { it.groupId == NOTIFY_SUCCESS && it.content == content && it.title == title }) {
            return // 已有相同通知，不再显示新通知
        }
        NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFY_SUCCESS)
            .createNotification(title, content, NotificationType.INFORMATION)
            .addAction(object : AnAction("查看文件") {
                override fun actionPerformed(e: AnActionEvent) {
                    files.forEach {
                        LocalFileSystem.getInstance().findFileByIoFile(it)?.let { virtualFile ->
                            FileEditorManager.getInstance(project).openFile(virtualFile, true)
                        }
                    }
                }
            })
            .notify(project)
    }

}