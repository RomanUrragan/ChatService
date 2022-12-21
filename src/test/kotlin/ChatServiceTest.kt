import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest(){

    @Before
    fun clearBeforeTest(){
        ChatService.clear()
    }

    @Test
    fun createChatTest(){
        var chat = ChatService.createChat(0, 1)
        assertTrue(ChatService.chats.contains(chat))
    }

    @Test
    fun getChatTest(){
        val chat1 = ChatService.createChat(100,200)
        val chat2 = ChatService.getChat(200,100)
        assertEquals(chat1, chat2)
    }

    @Test
    fun getChatTestNullResult(){
        assertNull(ChatService.getChat(100, 200))
    }

    @Test
    fun deleteChatTest(){
        val chat = ChatService.createChat(100, 200)
        ChatService.deleteChat(100, 200)
        assertFalse(ChatService.chats.contains(chat))
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatTestThrowException(){
        ChatService.deleteChat(100, 200)
    }

    @Test
    fun getChatsTest(){
        val chat1 = ChatService.createChat(100,200)
        val chat2 = ChatService.createChat(100,300)
        val chats = ChatService.getChats(100).sortedBy { it.id }
        assertEquals(chat1, chats[0])
        assertEquals(chat2, chats[1])
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatsTestThrowException(){
        val chats = ChatService.getChats(100).sortedBy { it.id }
    }

    @Test
    fun getChatMessages(){
        val mes1 = ChatService.createMessage(100, 200, "text1")
        val mes2 = ChatService.createMessage(200, 100, "text2")
        val messages = ChatService.getChatMessages(mes1.chatId, mes2.id, 1)
        assertEquals(mes2, messages[0])
    }

    @Test
    fun getChatMessagesIsRead(){
        val mes1 = ChatService.createMessage(100, 200, "text1")
        val mes2 = ChatService.createMessage(200, 100, "text2")
        ChatService.getChatMessages(mes1.chatId, mes2.id, 1)
        assertTrue(mes2.isRead)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatMessagesThrowChatNotFoundException(){
        ChatService.getChatMessages(1, 1, 1)
    }

    @Test(expected = MessageNotFoundException::class)
    fun getChatMessagesThrowMessageNotFoundException(){
        ChatService.createChat(100,200)
        ChatService.getChatMessages(1, 1, 1)
    }

    @Test
    fun getUnreadChatsCountTest(){
        val mes1 = ChatService.createMessage(100, 200, "text1")
        val mes2 = ChatService.createMessage(200, 100, "text2")
        val mes3 = ChatService.createMessage(100, 300, "text3")
        val mes4 = ChatService.createMessage(100, 400, "text2")
        ChatService.getChatMessages(mes1.chatId, mes2.id, 1)
        ChatService.getChatMessages(mes4.chatId,mes4.id,1)
        assertEquals(2, ChatService.getUnreadChatsCount(100))
    }

    @Test
    fun createMessageTestFirstMessage(){
        val mes = ChatService.createMessage(100,200, "text")
        assertEquals(mes, ChatService.chats[0].messages[0] )
    }

    @Test
    fun createMessageTestSecondMessage(){
        val mes1 = ChatService.createMessage(100,200, "text 1")
        val mes2 = ChatService.createMessage(100,200, "text 2")
        assertEquals(mes2, ChatService.chats[0].messages[1])
    }

    @Test
    fun editMessageTest(){
        val mes = ChatService.createMessage(100,200, "text 1")
        ChatService.editMessage(mes.id, "New text")
        assertEquals("New text", mes.text)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageThrowMessageNotFound(){
            ChatService.editMessage(1,"New text")
    }

    @Test
    fun deleteMessageFromMessagesTest() {
        val mes = ChatService.createMessage(100,200, "text 1")
        ChatService.deleteMessage(mes.id)
        assertFalse(ChatService.messages.contains(mes))
    }

    @Test
    fun deleteMessageFromChatTest() {
        val mes1 = ChatService.createMessage(100,200, "text 1")
        val mes2 = ChatService.createMessage(100,200, "text 2")
        ChatService.deleteMessage(mes2.id)
        assertFalse(ChatService.getChat(100, 200)!!.messages.contains(mes2))
    }

    @Test
    fun deleteLastMessageFromChatTest() {
        val mes1 = ChatService.createMessage(100,200, "text 2")
        ChatService.deleteMessage(mes1.id)
        assertTrue(ChatService.chats.isEmpty())
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageThrowMessageNotFoundException(){
        ChatService.deleteMessage(1)
    }

}