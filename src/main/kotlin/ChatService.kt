object ChatService {
    var messages: List<Message> = mutableListOf()
    var chats: MutableList<Chat> = mutableListOf()

    fun clear() {
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
        return chats.find {
            (it.firstUserId == firstUserId || it.firstUserId == secondUserId) &&
                    (it.secondUserId == firstUserId || it.secondUserId == secondUserId)
        }
    }

    //удаляет чат
    fun deleteChat(firstUserId: Int, secondUserId: Int) {
        var chat = getChat(firstUserId, secondUserId) ?: throw ChatNotFoundException("Чат не найден")
        chat.messages = emptyList()
        chats -= chat
    }

    //возвращает список чатов. Если чатов нет, то выбрасывает ошибку "нет сообщений"
    fun getChats(UserId: Int) = chats.filter { it.firstUserId == UserId || it.secondUserId == UserId }.ifEmpty {
        throw ChatNotFoundException("Нет чатов")
    }

    //Возвращает список последних сообщений в чатах
    fun getLastMessages(UserId: Int) = chats.filter { it.firstUserId == UserId || it.secondUserId == UserId }.map{chat -> chat.messages.last()}.ifEmpty {
            throw ChatNotFoundException("Нет сообщений")
    }


    //возвращает список сообщений в чате(id чата, id последнего сообшения, кол-во сообщений. Отданные сообщения = прочитаны
    fun getChatMessages(chatId: Int, lastMessageId: Int, count: Int): List<Message> {
        var chat = chats.find { it.id == chatId } ?: throw ChatNotFoundException("Чата с таким id не существует")
        return chat.messages
            .sortedBy() { it.id }
            .filter { it.id >= lastMessageId }
            .ifEmpty { throw MessageNotFoundException("Введен неверный id сообщения") }
            .take(count)
            .onEach { it.isRead = true }
    }

    //возвращает количество непрочитанных чатов (имеют непрочитанное сообщение)
    fun getUnreadChatsCount(userId: Int) = getChats(userId).count { chat -> chat.messages.any { !it.isRead } }

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
        var message =
            messages.find { it.id == messageId }
                ?: throw MessageNotFoundException("Сообщения с таким id не существует")
        message.text = text
    }

    //удаляет сообщение. При удалении последнего сообщения в чате весь чат удаляется.
    fun deleteMessage(messageId: Int) {
        var message = messages.find() { it.id == messageId }
            ?: throw MessageNotFoundException("Сообщения с таким id не существует")
        messages -= message
        var chat = getChat(message.fromUserId, message.toUserId) ?: throw ChatNotFoundException("Чат не найден")
        chat.messages -= message
        if (chat.messages.isEmpty()) {
            chats -= chat
        }
    }
}

