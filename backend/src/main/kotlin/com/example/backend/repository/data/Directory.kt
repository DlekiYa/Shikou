data class Directory(
    val isDir: Boolean,
    val name: String,

    val children: MutableList<Directory> = mutableListOf()
)