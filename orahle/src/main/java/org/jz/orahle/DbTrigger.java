package org.jz.orahle;

import java.util.List;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class DbTrigger extends SessionResolver {
    private String triggerName;

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerBody() {
        try 
        {
            List<String> source = this.getDbSession().getTriggerBody(this.getDbOwner(), this.triggerName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<pre>");
            for (String line : source) 
            {
                stringBuilder.append(line);
            }
            stringBuilder.append("</pre>");
            return stringBuilder.toString();
        } 
        catch(Exception ex) 
        {
            return "<font color='red'>Error reading trigger body</font>";
        }
    }

}
