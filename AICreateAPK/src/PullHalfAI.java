import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class PullHalfAI extends PullParseInterFace {
	public static String APKBUILDER_PATH = "apkbuilder";
	public static String APKNAME = "apkname";
	public static String KEYFILENAME = "keyfilename";
	public static String KEYPASS = "keypass";
	public static String KEYNICKNAME = "keynickname";
	
	public static String PATH = "path";
	
	private HashMap<String, String> tagMap = new HashMap<String, String>();

	/**
	 * 执行此方法将输入流传入Pull工场
	 * */
	@Override
	public void parserData(InputStream inputStream) throws Exception {
		super.parserData(inputStream);
		jsonObject = new JSONObject(strBuilder.toString().trim());
		startTag(jsonObject);
	}

	/**
	 * 实现此方法解析XML
	 * */
	@Override
	public void startTag(XmlPullParser parser) {

		for (int i = 0; i < parser.getAttributeCount(); i++) {
			if (parser.getAttributeName(i).equals(PATH)) {
				System.out.println("测试-StartTag = " + parser.getName().trim() + " : " + parser.getAttributeValue(i).trim());
				tagMap.put(parser.getName().trim(), parser.getAttributeValue(i).trim());
			}
		}
	}

	/**
	 * 获得数据
	 * */
	public HashMap<String, String> getTagMap() {
		return tagMap;
	}

	@Override
	public void startTag(JSONObject jsonObject) throws Exception{
		System.out.println("jsonOBject = "+ jsonObject);
		HalfAI.apkbuilder_path = jsonObject.getString(APKBUILDER_PATH);
		HalfAI.ApkName = jsonObject.getString(APKNAME);
		HalfAI.keyfilename = jsonObject.getString(KEYFILENAME);
		HalfAI.keypass = jsonObject.getString(KEYPASS);
		HalfAI.keyNickName = jsonObject.getString(KEYNICKNAME);
	}

	@Override
	public void startTag(JSONArray jsonArray) {
		
	}

}
