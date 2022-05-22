package vocabulary.bot.botapi;

import lombok.Getter;
import lombok.ToString;
import vocabulary.bot.botapi.handlers.*;

@Getter
@ToString
public enum BotState {

  //Неопределенная команда
  UNDEFINED(1, null, new UndefinedHandler(), false, null),

  //Ожидание пользователя
  WAITING(2, null, null, false, null),

  //Обработчик слова
  WORD(3, null, new WordHandler(), false, null),

  //Обработчики команд
  START(4, "/start", new StartHandler(), false, null),
  HELP(5, "/help", new HelpHandler(), false, null),

  //Обработчики команд для меню
  LEARN(6, "/learn", new LearnHandler(), true, "получить слово"),
  TRANSLATE(7, "/translate", new TranslateHandler(), true, "перевод"),
  TEST(8, "/test", new TestHandler(), true, "пройти тест"),
  STATISTIC(9, "/statistic", new StatisticHandler(), true, "получить статистику"),
  INFO(10, "/info", new InfoHandler(), true, "дополнительная информация");

  private final int botStateId;
  private final String activationKey;
  private final InputMessageHandler handler;
  private final boolean showInMenu;
  private final String description;


  BotState(int botStateId, String activationKey, InputMessageHandler handler, boolean showInMenu, String description) {
    this.botStateId = botStateId;
    this.activationKey = activationKey;
    this.handler = handler;
    this.showInMenu = showInMenu;
    this.description = description;
  }

  public static BotState getBotStateByActivationKey(String activationKey) {
    for (BotState state : BotState.values()) {
      if (state.activationKey != null && state.activationKey.equals(activationKey)) {
        return state;
      }
    }
    if (activationKey.charAt(0) == '/') return UNDEFINED;
    return BotState.WORD;
  }

  public static BotState getBotStateById(int botStateId) {
    for (BotState botState : BotState.values()) {
      if (botState.getBotStateId() == botStateId) return botState;
    }
    return null;
  }

}
