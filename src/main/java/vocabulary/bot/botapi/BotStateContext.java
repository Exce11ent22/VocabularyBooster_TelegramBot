package vocabulary.bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

public class BotStateContext {

  private final Map<BotState, InputMessageHandler> messageHandlers;

  public BotStateContext(Map<BotState, InputMessageHandler> messageHandlers) {
    this.messageHandlers = messageHandlers;
  }

  public SendMessage processInputMessage(BotState currentState, Message message) {
    InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
    return currentMessageHandler.handle(message);
  }

  private InputMessageHandler findMessageHandler(BotState currentState) {
    return messageHandlers.get(currentState);
  }

}
