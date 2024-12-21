package authentication_project.fido.auth.usecase.token

interface TokenProvider {
    fun createToken(userId:Long, email:String): String
    fun verifyToken(token:String): Boolean
    fun getUserId(token:String): Long
}