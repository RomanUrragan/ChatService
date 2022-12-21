var nextMessageId: Int = 1

class Message(
    var fromUserId: Int,
    var toUserId: Int,
    var text: String,
    var isRead: Boolean = false
) {
    var chatId = -1
    var id = nextMessageId++
}