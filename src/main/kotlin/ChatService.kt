object ChatService {
    var messages: List<Message> = mutableListOf()
    var chats: List<Chat> = mutableListOf()

    fun clear(){
        messages = mutableListOf()
        chats = mutableListOf()
        nextMessageId = 1
        nextChatId = 1
    }

    //создает чат
    fun createChat(firstUserId: Int, secondUserId: Int): Chat {
        var chat = Chat(firstUserId, secondUserId)
        chats += chat
        return chat
    }

    //возвращает чат между двумя пользователями
    fun getChat(firstUserId: Int, secondUserId: Int): Chat? {
        return chats.find { (it.firstUserId == firstUserId || it.firstUserId == secondUserId) &&
                (it.secondUserId == firstUserId || it.secondUserId == secondUserId) }
    }

    //удаляет чат
    fun deleteChat(firstUserId: Int, secondUserId: Int) {
        var chat = getChat(firstUserId, secondUserId) ?: throw ChatNotFoundException("Чат не найден")
        chat.messages = emptyList()
        chats -= chat
    }

    //возвращает список чатов. Если чатов нет, то выбрасывает ошибку "нет сообщений"
    fun getChats(UserId: Int): List<Chat> {
        var foundChats = chats.filter { it.firstUserId == UserId || it.secondUserId == UserId }
        return foundChats.ifEmpty {
            throw ChatNotFoundException("Нет сообщений")
        }
    }

    //возвращает список сообщений в чате(id чата, id последнего сообшения, кол-во сообщений. Отданные сообщения = прочитаны
    fun getChatMessages(chatId: Int, lastMessageId: Int, count: Int): List<Message> {
        var foundChat = chats.filter { it.id == chatId }
        if (foundChat.isEmpty()) {
            throw ChatNotFoundException("Чата с таким id не существует")
        } else {
            var chat: Chat = foundChat.first()
            var messages = chat.messages
                .sortedBy() { it.id }
                .filter { it.id >= lastMessageId }
                .filterIndexed { index, _ -> index < count }
            if (messages.isEmpty()) {
                throw MessageNotFoundException("Введен неверный id сообщения")
            }
            for (message in messages) {
                message.isRead = true
            }
            return messages
        }
    }

    //возвращает количество непрочитанных чатов (имеют непрочитанное сообщение)
    fun getUnreadChatsCount(userId: Int): Int {
        var count = 0
        var userChats = getChats(userId)
        for (chat in userChats) {
            for (message in chat.messages) {
                if (!message.isRead) {
                    count++
                    break
                }

            }
        }
        return count
    }

    //создает сообщение и возвращает его
    fun createMessage(fromUserId: Int, toUserId: Int, text: String): Message {
        val message = Message(fromUserId, toUserId, text)
        messages += message
        var chat = getChat(fromUserId, toUserId) ?: createChat(fromUserId, toUserId)
        message.chatId = chat.id
        chat.messages += message
        return message
    }

    //редактирует сообщение
    fun editMessage(messageId: Int, text: String) {
        var message = messages.find { it.id == messageId } ?: throw MessageNotFoundException("Сообщения с таким id не существует")
        message.text = text
    }

    //удаляет сообщение. При удалении последнего сообщения в чате весь чат удаляется.
    fun deleteMessage(messageId : Int) {
        var message = messages.find() {it.id == messageId} ?: throw MessageNotFoundException("Сообщения с таким id не существует")
        messages -= message
        var chat = getChat(message.fromUserId, message.toUserId)?: throw ChatNotFoundException("Чат не найден")
        chat.messages -= message
        if (chat.messages.isEmpty()) {
            chats -= chat
        }
    }
}

