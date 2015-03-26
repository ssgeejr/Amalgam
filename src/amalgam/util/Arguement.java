package amalgam.util;

import java.io.Serializable;

public class Arguement implements Serializable{
  private String name;
  private String value;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}