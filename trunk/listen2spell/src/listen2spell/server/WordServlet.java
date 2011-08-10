package listen2spell.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import listen2spell.client.WordService;
import listen2spell.shared.Spoken;

@SuppressWarnings("serial")
public class WordServlet extends RemoteServiceServlet implements WordService {

  private static final String[] ARCHER_WORDS_2010_12_10 = {
      "Girl", "Clear", "Her", "Turn", "Dark", "Work", "Smart", "Spoken", "Hurt", "Serve", "North",
      "Third",};
  private static final String[] ARCHER_WORDS_2010_12_13 = {
      "Clown", "Lawn", "Talk", "Sound", "Cloth", "Would", "Also", "Mouth", "Crown", "Soft",
      "Count", "Law",};
  private static final String[] ARCHER_WORDS_2011_01_17 = {
      "Large", "Gym", "Skin", "Quick", "Picnic", "Judge", "Park", "Jeans", "Crack", "Orange",
      "Second", "Squeeze",};
  private static final String[] ARCHER_WORDS_2011_01_28 = {
      "Hair", "Care", "Chair", "Pair", "Bear", "Where", "Scare", "Air", "Pear", "Bare", "Fair",
      "Share",};
  private static final String[] ARCHER_WORDS_2011_01_31 = {
      "Cared", "Babies", "Chopped", "Saving", "Carried", "Fixing", "Hurried", "Joking", "Grinning",
      "Smiled", "Wrapped", "Parties",};
  private static final String[] ARCHER_WORDS_2011_02_11 = {
      "Untrue", "Review", "Unhealthy", "Quietly", "Graceful", "Wonderful", "Skier", "Correctly",
      "Safely", "Horribly", "Uncover", "Clearly",};
  private static final String[] ARCHER_WORDS_2011_02_18 = {
      "Tooth", "Chew", "Grew", "Cook", "Shoe", "Blue", "Boot", "Flew", "Shook", "Balloon", "Drew",
      "Spoon",};
  private static final String[] ARCHER_WORDS_2011_03_04 = {
      "Caught", "Thought", "Bought", "laugh", "Through", "Enough", "Caught", "Daughter", "Taught",
      "Brought", "Ought", "Cough",};
  private static final String[] ARCHER_WORDS_2011_03_11 = {
      "Monday", "Sudden", "Until", "Forget", "Happen", "Follow", "Dollar", "Window", "Hello",
      "Market", "Pretty", "Order",};

  public Spoken getSpokenWord(String word) {
    return Speech.getWord(word);
  }

  @Override
  public String[] getWordList() {
    return ARCHER_WORDS_2011_03_11;
  }
}
