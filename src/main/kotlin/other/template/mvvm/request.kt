package other.template.mvvm

import java.util.Locale

/**
 * 创建请求函数
 *
 * @param className 类名
 * @param params 参数
 * @param dataName 数据类名 className.replaceFirstChar { if (it.isLowerCase()) it.lowercase(Locale.ROOT) else it.toString() }
 * @param httpType 请求类型
 * @param isList 是否是列表
 */
@OptIn(ExperimentalStdlibApi::class)
fun requestKt(
    className: String,
    params: List<Pair<String, String>>,
    dataName: String,
    httpType: String,
    url: String,
    isList: Boolean
) = """
    fun get$className(${params.toParams()}){
        request{
            val result = Get<${if (isList) "List<$className>" else className}>("$url").await()
            _$dataName.emit(result)
        }
    }
""".trimIndent()

fun requestKt(
    className: String,
    dataName: String,
    isList: Boolean
) = """
    private val _$dataName = MutableSharedFlow<${if (isList) "List<$className>" else className}>()
    val $dataName = _$dataName.asSharedFlow()
""".trimIndent()

private fun List<Pair<String, String>>.toParams() =
    joinToString(", ") { (name, type) -> "$name: $type" }

fun requestImportKt(
    classPackageName: String,
    httpType: String
) = """
import com.fortune.library.net.$httpType
import $classPackageName
""".trimIndent()
