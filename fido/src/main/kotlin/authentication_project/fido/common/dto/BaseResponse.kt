package authentication_project.fido.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
class BaseResponse<T>(
    val resultCode: Int,
    val resultMessage: String,
    val resultData: T? = null
) {
}