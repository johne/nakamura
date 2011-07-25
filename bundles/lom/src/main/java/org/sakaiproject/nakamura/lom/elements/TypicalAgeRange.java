package org.sakaiproject.nakamura.lom.elements;

import org.apache.sling.commons.json.JSONObject;
import org.sakaiproject.nakamura.lom.type.LangStringType;

public class TypicalAgeRange extends LangStringType {

  public TypicalAgeRange() {
    super();
  }
  
  public TypicalAgeRange(JSONObject json) {
    super(json);
  }
}
