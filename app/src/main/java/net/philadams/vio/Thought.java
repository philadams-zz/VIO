package net.philadams.vio;

import java.util.Date;

/**
 * Created by phil on 10/21/14.
 */
public class Thought {

  public String thinker;
  public Date when;

  public String toString() {
    return String.format("Thought[thinker=%s, when=%s]", thinker, when.toString());
  }

}
