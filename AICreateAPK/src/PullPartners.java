import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

public class PullPartners extends PullParseInterFace {

	public static String VALUE = "value";
	public static String ITEM = "item";

	private ArrayList<String> partnerList;

	/**
	 * 装载流
	 * */
	@Override
	public void parserData(InputStream inputStream) throws Exception {
		partnerList = new ArrayList<String>();
		super.parserData(inputStream);
		jsonArray = new JSONArray(strBuilder.toString().trim());
		startTag(jsonArray);
	}

	/**
	 * 实现具体解析
	 * */
	@Override
	public void startTag(XmlPullParser parser) {
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			if (parser.getAttributeName(i).equals(VALUE)) {
				System.out.println("测试-StartTag = " + parser.getName().trim() + " : " + parser.getAttributeValue(i).trim());
				partnerList.add(parser.getAttributeValue(i).trim());
			}
		}
	}

	/**
	 * 获得集合
	 * */
	public ArrayList<String> getList() {
		return partnerList;
	}

	@Override
	public void startTag(JSONArray jsonArray) throws Exception{
		for(int i = 0; i < jsonArray.length(); i++){
			partnerList.add(jsonArray.getString(i));
		}
	}

	@Override
	public void startTag(JSONObject jsonObject) {
		
	}

}
