package org.sakaiproject.nakamura.cp;

import org.apache.sling.commons.json.JSONObject;
import org.sakaiproject.nakamura.lom.type.JSONUtil;
import org.sakaiproject.nakamura.lom.type.Serialize;

public class Dependency extends Serialize {

  private String identifierref;
  
  public Dependency() {
    super();
  }
  
  public Dependency(JSONObject json) {
    super(json);
  }
  @Override
  protected void init() {
    String identifierrefName = "identifierref";
    identifierref = JSONUtil.getStringValue(json, identifierrefName);    
  }
  
  public String getIdentiferRef() {
    return identifierref;
  }
  
  public void setIdentifierRef(String id) {
    this.identifierref = id;
  }
}
