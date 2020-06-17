package services.yaml.data

data class UserItem(
    val admin: Boolean,
    val email: String,
    val name: String,
    val roles: List<String>
)