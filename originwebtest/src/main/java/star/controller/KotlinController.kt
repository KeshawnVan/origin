package star.controller

import star.annotation.bean.Controller
import star.annotation.bean.Inject
import star.annotation.controller.Action
import star.annotation.controller.Stream
import star.bean.User
import star.service.TestService

/**
 * @author keshawn
 * @date 2018/2/2
 */
@Controller
open class KotlinController {

    @Inject
    private var testService: TestService? = null

    @Action("kt")
    @Stream
    fun testKt(): List<User> {
        val users = testService?.findByNamesAndAge()
        return users.orEmpty()
    }
}

