package vocabulary.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.TelegramFacade;
import vocabulary.bot.tools.ResourcesAccess;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VocabularyBoosterBot extends TelegramLongPollingBot {

  private final TelegramFacade telegramFacade;
  private final List<BotCommand> commandsList;

  public VocabularyBoosterBot(TelegramFacade telegramFacade) {
    this.telegramFacade = telegramFacade;
    commandsList = new ArrayList<>();
    for (BotState botState : BotState.values()) {
      if (botState.isShowInMenu()) {
        commandsList.add( new BotCommand(botState.getActivationKey(), botState.getDescription()));
      }
    }
  }

  @Override
  public String getBotUsername() {
    return ResourcesAccess.getFromResources("bot_username");
  }

  @Override
  public String getBotToken() {
    return ResourcesAccess.getFromResources("token");
  }

  @Override
  public void onUpdateReceived(Update update) {
    new Thread(() -> {
      if (!update.hasMessage() && update.getMessage().hasText()) return;
      SendMessage replyMessage = telegramFacade.handleUpdate(update);
      try {
        execute(replyMessage); // Call method to send the message
        execute(new SetMyCommands(commandsList, new BotCommandScopeChat(update.getMessage().getChatId().toString()), "ru"));
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }).start();
  }
}
