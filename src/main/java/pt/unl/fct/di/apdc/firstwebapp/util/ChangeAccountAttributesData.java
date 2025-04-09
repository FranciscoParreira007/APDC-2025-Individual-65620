package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.Map;

public class ChangeAccountAttributesData {

    public String requesterToken;
    public String targetToken;
    public Map<String, String> attributes;

    public ChangeAccountAttributesData(){}

    public ChangeAccountAttributesData(String requesterToken, String targetToken, Map<String, String> attributes){
        this.requesterToken = requesterToken;
        this.targetToken = targetToken;
        this.attributes = attributes;
    }
}
