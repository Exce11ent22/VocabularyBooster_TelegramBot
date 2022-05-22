package vocabulary.bot.service;

import org.json.JSONArray;
import org.json.JSONObject;
import vocabulary.bot.tools.HttpContentReader;
import vocabulary.bot.tools.ResourcesAccess;

import java.io.IOException;
import java.util.Locale;

public class DictionaryAPI {

  public String getWordInfo(String word) {
    StringBuilder stringBuilder = new StringBuilder(word.toUpperCase(Locale.ROOT));
    stringBuilder.append("\n");
    JSONObject jsonObject = getJson(word);

    if (jsonObject == null) return null;
    if (!jsonObject.has("def")) return null;

    JSONArray def = jsonObject.getJSONArray("def");
    if (def.isEmpty()) return null;

    stringBuilder.append(getTranscription(def, 0));
    stringBuilder.append(getTranslateInfo(def, 0));

    return stringBuilder.toString();
  }

  public String getTranslate(String word) {
    JSONObject jsonObject = getJson(word);

    if (jsonObject == null) return null;
    if (!jsonObject.has("def")) return null;

    JSONArray def = jsonObject.getJSONArray("def");
    if (def.isEmpty()) return null;

    JSONObject wordInfo = def.getJSONObject(0);
    if (wordInfo.isEmpty()) return null;
    if (wordInfo.has("tr") && !wordInfo.getJSONArray("tr").isEmpty()) {
      return wordInfo.getJSONArray("tr").getJSONObject(0).getString("text");
    }
    return null;
  }

  private String getTranslateInfo(JSONArray def, int n) {
    StringBuilder stringBuilder = new StringBuilder();
    if (def.getJSONObject(n).has("tr") && !def.getJSONObject(n).getJSONArray("tr").isEmpty()) {
      JSONArray translate = def.getJSONObject(n).getJSONArray("tr");
      for (int i = 0; i < translate.length(); i++) {
        if (translate.getJSONObject(i).has("text")) {
          stringBuilder.append("\n✅ Перевод: ")
            .append(translate.getJSONObject(i).getString("text"))
            .append("\n");

          if (translate.getJSONObject(i).has("pos")) {
            stringBuilder.append("Часть речи: [")
              .append(translate.getJSONObject(i).getString("pos"))
              .append("]\n");
          }

        }
        stringBuilder.append(getMean(translate, i));
        stringBuilder.append(getExample(translate, i));
      }


    }
    return stringBuilder.toString();
  }

  private String getExample(JSONArray translate, int n) {
    StringBuilder stringBuilder = new StringBuilder();
    if (translate.getJSONObject(n).has("ex")) {
      JSONArray example = translate.getJSONObject(n).getJSONArray("ex");
      if (!example.isEmpty()) {
        stringBuilder.append("\nПример использования:\n");
        for (int i = 0; i < example.length(); i++) {
          stringBuilder.append(i + 1)
            .append(". ")
            .append(example.getJSONObject(i).getString("text"));
          if (example.getJSONObject(i).has("tr") && !example.getJSONObject(i).getJSONArray("tr").isEmpty()) {
            stringBuilder.append(" (")
              .append(example.getJSONObject(i).getJSONArray("tr").getJSONObject(0).getString("text"))
              .append(")");
          }
          stringBuilder.append("\n");
        }
      }
    }
    return stringBuilder.toString();
  }

  private String getMean(JSONArray translate, int n) {
    StringBuilder stringBuilder = new StringBuilder();
    if (translate.getJSONObject(n).has("mean")) {
      JSONArray mean = translate.getJSONObject(n).getJSONArray("mean");
      if (!mean.isEmpty()) {
        stringBuilder.append("\nВозможно имеется ввиду:\n");
        for (int i = 0; i < mean.length(); i++) {
          stringBuilder.append(i + 1)
            .append(". ")
            .append(mean.getJSONObject(i).getString("text"))
            .append("\n");
        }
      }
    }
    return stringBuilder.toString();
  }

  private String getTranscription(JSONArray def, int i) {
    StringBuilder stringBuilder = new StringBuilder();
    if (def.getJSONObject(i).has("ts")) {
      stringBuilder.append("\nТранскрипция - [")
        .append(def.getJSONObject(i).getString("ts"))
        .append("]\n");
    }
    return stringBuilder.toString();
  }

  private JSONObject getJson(String word) {
    String content;
    try {
      content = HttpContentReader.read(ResourcesAccess.getFromResources("dictionary_api_uri") + word);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return new JSONObject(content);
  }

}
