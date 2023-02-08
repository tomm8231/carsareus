package dat3.experiments;

public class SimpleSanitizer {
  public static String simpleSanitize(String s){
    return s.replaceAll("<b>", "").replaceAll("</b>", "");
    //Kan også gøres med regex på en mere generel måde, så den finder alt der start er med "<" og slutter med ">"
  }
}

