package anthony.libs.chatapp.server.processor;

import anthony.libs.chatapp.core.container.CachedMessages;
import anthony.libs.chatapp.core.message.MessageInfo;
import anthony.libs.chatapp.core.message.OperationMessage;
import anthony.libs.chatapp.core.processor.AbstractOperationMessageProcessor;
import anthony.libs.chatapp.server.ClientInfo;
import anthony.libs.chatapp.server.container.OnlineClientInfoContainer;

/**
 * Created by chend on 2017/8/14.
 */
public class ServerOperationMessageProcessor extends AbstractOperationMessageProcessor {

    private OnlineClientInfoContainer onlineClientInfoContainer = OnlineClientInfoContainer.getInstance();
    private CachedMessages cachedMessages = CachedMessages.getInstance();

    @Override
    protected void doProcess(MessageInfo messageInfo) {
        System.out.println("OperationMessageProcessor");
        OperationMessage message= (OperationMessage) messageInfo.getMessage();
        switch (message.getOperation()) {
            case LOGIN:
                login(message);
                break;
            case ACK:
                break;
            case ACK_ACK:
                break;
        }
    }

    private void login(OperationMessage message) {
        ClientInfo newClientInfo = new ClientInfo();
//        newClientInfo.setSelectionKey(getSelectionKey());
        newClientInfo.setUserInfo(message.getUserInfo());
        ClientInfo oldClientInfo = onlineClientInfoContainer.addClientInfo(newClientInfo);
        //顶掉以前的登陆信息
        if (null != oldClientInfo) {
            OperationMessage antherLogin = new OperationMessage(OperationMessage.TYPE.ANOTHER_LOGIN);
            antherLogin.setDestination(oldClientInfo.getUserInfo().getUserId());
            messageQueue.put(antherLogin);
        }
//            MessageUtil.sendMessage(new OperationMessage(OperationMessage.TYPE.ANOTHER_LOGIN),
//                    (SocketChannel) oldClientInfo.getSelectionKey().channel());
        //发送登陆成功确认
        OperationMessage loginSuccess = new OperationMessage(OperationMessage.TYPE.LOGIN_SUCCESS);
        loginSuccess.setDestination(newClientInfo.getUserInfo().getUserId());
        messageQueue.put(loginSuccess);
        cachedMessages.sendCachedMessage(newClientInfo.getUserInfo().getUserId());
//        MessageUtil.sendMessage(new OperationMessage(OperationMessage.TYPE.LOGIN_SUCCESS),
//                (SocketChannel) getSelectionKey().channel());
    }

    private void ack(OperationMessage message) {
        //删除缓存的message 缓存ack 发送ackack

    }

    private void ackAck(OperationMessage message) {
        //删除缓存的ack
    }

}
