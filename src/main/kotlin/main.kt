fun main(){
    var mes1 = ChatService.createMessage(1,2,"text 1")
    var mes2 = ChatService.createMessage(2,1,"text 2")
    var mes3 = ChatService.createMessage(1,3,"text 3")
    var mes4 = ChatService.createMessage(2,1,"text 4")
    var mes5 = ChatService.createMessage(2,1,"text 5")
    var mes6 = ChatService.createMessage(2,1,"text 6")
    var mes7 = ChatService.createMessage(2,3,"text 7").id

    var id = ChatService.getChat(1,3)!!.id
    var messages1 = ChatService.getChatMessages(id, 0,1)
    for (message in messages1){
       println(message.text + " isRead")
    }

    println(ChatService.getUnreadChatsCount(1))

}