package authentication_project.fido.fido.dto

data class ServerAuthenticatorAssertionResponse(
    val authenticatorData: String,
    val clientDataJSON: String,
    val signature: String,
    val userHandle: String
)
//각각의 역할이 뭐야
//authenticatorData: 서명된 데이터의 일부로, 인증기기의 상태와 사용자의 인증을 확인하는 데 사용됩니다.
//clientDataJSON: 서명된 데이터의 일부로, 클라이언트에서 생성된 데이터입니다.
//signature: 서명된 데이터의 일부로, 서명된 데이터의 나머지 부분을 확인하는 데 사용됩니다.
//userHandle: 서명된 데이터의 일부로, 사용자의 식별자입니다. 사용자가 인증기기에 등록되어 있지 않은 경우 null일 수 있습니다.
