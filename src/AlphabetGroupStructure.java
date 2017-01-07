/**
 * Created by sxh on 28.12.16.
 */
public class AlphabetGroupStructure implements GroupStructure<String> {
  @Override
  public String mul(String a, String b) {
    return AlphabetGroupStructure.simplify(a+b);
  }

  @Override
  public String inv(String a) {
    String result = "";
    for (Character c : a.toCharArray())
      result = String.valueOf(Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c)) + result;
    return result;
  }

  @Override
  public String unit() {
    return "";
  }

  public static String simplify(String result) {
    boolean needsRepeat;
    do {
      needsRepeat = false;
      boolean skipFlag = false;
      String step = "";
      for (int i = 0; i < result.length() - 1; i++) {
        if (skipFlag) {
          skipFlag = false;
          continue;
        }
        char c = result.charAt(i);
        char d = result.charAt(i + 1);
        if (Character.toUpperCase(c) == Character.toUpperCase(d) &&
          Character.isUpperCase(c) ^ Character.isUpperCase(d)) {
          skipFlag = true;
          needsRepeat = true;
        } else {
          step += c;
        }
      }
      if (result.length() > 0 && !skipFlag) step += result.charAt(result.length() - 1);
      result = step;
    } while (needsRepeat);
    return result;
  }
}
