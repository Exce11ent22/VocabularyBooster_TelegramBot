package vocabulary.bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import vocabulary.bot.dao.UserRepository;

@Slf4j
public class TelegramFacade {

  private final UserRepository userRepository;

  public TelegramFacade(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public SendMessage handleUpdate(Update update) {
    SendMessage replyMessage = null;

    Message message = update.getMessage();
    if (message != null && message.hasText()) {
      log.info("New message from User:{}, chatId: {},  with text: {}",
        message.getFrom().getUserName(), message.getChatId(), message.getText());
      replyMessage = handleInputMessage(message);
    }

    return replyMessage;
  }

  private SendMessage handleInputMessage(Message message) {
    String inputMsg = message.getText();
    long userId = message.getFrom().getId();
    BotState botState;
    SendMessage replyMessage;

    botState = userRepository.getBotStateOfUser(userId);
    if (botState == BotState.WAITING) {
      botState = BotState.getBotStateByActivationKey(inputMsg);
    }

    userRepository.setBotStateForUser(userId, botState);

    replyMessage = botState.getHandler().handle(message);

    return replyMessage;
  }

}
