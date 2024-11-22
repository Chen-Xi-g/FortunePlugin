package other.dialog

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent

class RequestDialog(val path: String) : DialogWrapper(true) {

    private lateinit var methodNameField: JTextField
    private lateinit var apiPathField: JTextField
    private lateinit var requestTypeComboBox: ComboBox<String>
    private lateinit var isListCheckBox: JCheckBox
    private var parametersPanel = JPanel(GridLayout(0, 3, 5, 5))
    private val jsonBodyField = JTextArea(5, 30)
    private val parametersScrollPane = JBScrollPane(parametersPanel)

    init {
        title = "通过JSON生成请求函数和实体类"
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val mainPanel = JPanel(BorderLayout())
        mainPanel.preferredSize = Dimension(800, 600)

        val paramPanelParent = JPanel(BorderLayout())

        // 创建参数面板
        val paramPanel = JPanel()
        paramPanel.layout = BoxLayout(paramPanel, BoxLayout.X_AXIS)  // 垂直排列

        // 方法名输入
        val methodNamePanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 10))
        methodNamePanel.add(JLabel("实体类名称"))
        methodNameField = JTextField(20)
        methodNamePanel.add(methodNameField)

        // 接口地址
        val apiPathPanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 10))
        apiPathPanel.add(JLabel("接口地址"))
        apiPathField = JTextField(20)
        apiPathPanel.add(apiPathField)

        // 请求类型
        val requestTypePanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 10))
        requestTypePanel.add(JLabel("请求类型"))
        // 选择框, 默认为GET, 可选POST/PUT/DELETE
        requestTypeComboBox = ComboBox<String>().apply {
            addItem("GET")
            addItem("POST")
            addItem("PUT")
            addItem("DELETE")
        }
        requestTypePanel.add(requestTypeComboBox)

        // 参数输入区域
        val methodParamsPanel = JPanel(BorderLayout())
        parametersPanel.add(JLabel("参数名"))
        parametersPanel.add(JLabel("参数类型"))
        parametersPanel.add(JButton("新增").apply {
            addActionListener { addParameterRow() }
        })
        methodParamsPanel.add(JLabel("请求参数: ").apply {
            border = JBUI.Borders.empty(10,5)
        },BorderLayout.NORTH)
        methodParamsPanel.add(parametersScrollPane,BorderLayout.CENTER)

        paramPanel.add(requestTypePanel)
        paramPanel.add(methodNamePanel)
        paramPanel.add(apiPathPanel)
        paramPanelParent.add(paramPanel, BorderLayout.NORTH)
        paramPanelParent.add(methodParamsPanel, BorderLayout.CENTER)



        // 方法体输入区域
        val methodBodyPanel = JPanel(BorderLayout())
        jsonBodyField.lineWrap = true
        jsonBodyField.wrapStyleWord = true
        methodBodyPanel.add(JLabel("响应体JSON数据").apply {
            border = JBUI.Borders.empty(10,5)
        }, BorderLayout.NORTH)
        methodBodyPanel.add(JScrollPane(jsonBodyField), BorderLayout.CENTER)

        // 当前生成的路径
        val currentPathPanel = JPanel(FlowLayout(FlowLayout.LEFT, 5, 10))
        currentPathPanel.add(JLabel("当前生成的路径: "))
        currentPathPanel.add(JTextPane().apply {
            text = path
            isEditable = false
            background = JBColor(Color(0x505050), Color(0x505050))
            border = BorderFactory.createCompoundBorder(
                EmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color(0x505050))
            )
        })
        isListCheckBox = JCheckBox("是否为集合数据?")
        currentPathPanel.add(isListCheckBox)

        // 将各部分添加到主面板
        mainPanel.add(paramPanelParent, BorderLayout.NORTH)
        mainPanel.add(methodBodyPanel, BorderLayout.CENTER)
        mainPanel.add(currentPathPanel, BorderLayout.SOUTH)

        return mainPanel
    }

    override fun show() {
        super.show()
        adjustDialogHeight()
    }

    private fun addParameterRow() {
        val nameField = JTextField(10)
        val typeField = ComboBox<String>().apply {
            addItem("String")
            addItem("Boolean")
            addItem("Int")
            addItem("Long")
            addItem("Float")
            addItem("Double")
        }
        val removeButton = JButton("删除").apply {
            addActionListener {
                parametersPanel.remove(nameField)
                parametersPanel.remove(typeField)
                parametersPanel.remove(this)
                parametersPanel.revalidate()
                parametersPanel.repaint()
                adjustDialogHeight() // 删除时重新调整高度
            }
        }
        parametersPanel.add(nameField)
        parametersPanel.add(typeField)
        parametersPanel.add(removeButton)
        parametersPanel.revalidate()
        adjustDialogHeight() // 添加时重新调整高度
    }

    // 动态调整对话框高度
    private fun adjustDialogHeight() {
        // 获取当前参数面板的实际高度
        val preferredHeight = parametersPanel.preferredSize.height + 20
        val maxVisibleHeight = 200 // 设置滚动区域的最大高度

        // 调整滚动面板的高度
        val newHeight = if (preferredHeight > maxVisibleHeight) maxVisibleHeight else preferredHeight
        parametersScrollPane.preferredSize = Dimension(parametersScrollPane.preferredSize.width, newHeight)

        // 调整对话框高度
        pack() // 重新计算窗口大小
    }

    override fun doOKAction() {
        val message = if (methodNameField.text.isBlank()){
            "实体类名称不能为空"
        }else if (apiPathField.text.isBlank()){
            "接口地址不能为空"
        }else if (jsonBodyField.text.isBlank()){
            "响应体JSON数据不能为空"
        }else{
            ""
        }

        if (message.isNotEmpty()){
            Messages.showErrorDialog(message, "提示")
            return
        }
        super.doOKAction()
    }

    /**
     * 获取请求类型
     */
    fun getHttpType(): String {
        return requestTypeComboBox.selectedItem!!.toString()
    }

    /**
     * 获取实体类名称
     */
    fun getMethodName(): String {
        return methodNameField.text
    }

    /**
     * 获取接口地址
     */
    fun getApiPath(): String {
        return apiPathField.text
    }

    /**
     * 获取请求参数
     */
    fun getParams(): List<Pair<String, String>> {
        val params = mutableListOf<Pair<String, String>>()
        for (i in 0 until parametersPanel.componentCount step 3) {
            val name = (parametersPanel.getComponent(i) as? JTextField)?.text
            val type = (parametersPanel.getComponent(i + 1) as? ComboBox<String>)?.selectedItem?.toString()
            if (!name.isNullOrEmpty() && !type.isNullOrEmpty()){
                params.add(name to type)
            }
        }
        return params
    }

    /**
     * 获取响应体JSON数据
     */
    fun getJsonBody(): String {
        return jsonBodyField.text
    }

    /**
     * 是否为集合
     */
    fun isList(): Boolean{
        return isListCheckBox.isSelected
    }

}