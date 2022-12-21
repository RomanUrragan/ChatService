import java.util.*
var nextChatId = 1

class Chat(
    var firstUserId: Int,
    var secondUserId: Int,
    var messages: List<Message> = mutableListOf()
) {
    var id: Int = nextChatId++
}