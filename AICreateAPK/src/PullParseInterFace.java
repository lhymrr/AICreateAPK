import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public abstract class PullParseInterFace {
	public StringBuilder strBuilder;
	public JSONArray jsonArray;
	public JSONObject jsonObject;
	public void parserData(InputStream inputStream) throws Exception {
		strBuilder = new StringBuilder();
		
		BufferedReader in = new BufferedReader (new InputStreamReader (inputStream, "UTF-8"));
		String tempstr = "";
		do{
			strBuilder.append(tempstr.trim());
			tempstr = in.readLine();
		}while(tempstr != null);
		in.close();
		
	}
	abstract public void startTag(JSONObject jsonObject) throws Exception;
	abstract public void startTag(JSONArray jsonArray) throws Exception;
	abstract public void startTag(XmlPullParser parser) throws Exception;
}
