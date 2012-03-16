import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class HalfAI {

	/**
	 * 工作空间目录，既Create_HalfAI目录 Path for workspaces
	 * */
	public static String Create_APK_in_Half_AI_Path;

	/**
	 * Letv.properties绝对路径 ： /properties_files/letv.properties
	 * */
	public static String LetvProperties_path;

	/**
	 * halfAI.ini绝对路径
	 * */
	public static String HALFAI_INI_PATH;

	/**
	 * android tools 目录
	 * */
	public static String ANDROID_TOOLS_PATH;

	/**
	 * Letv.propters中Letv.partner=字符串
	 * */
	public static String PARTNERKEY = "letv.partner=";

	/**
	 * properties_config.json 绝对路径
	 * */
	public static String propConfigPath;

	/**
	 * HalfAI.json中的参数内容
	 * */
	/**
	 * apkbuilder.bat所在路径
	 * */
	public static String apkbuilder_path;

	/**
	 * apk名称
	 * */
	public static String ApkName;

	/**
	 * 签名文件名称
	 * */
	public static String keyfilename;

	/**
	 * 签名文件密码
	 * */
	public static String keypass;

	/**
	 * 签名昵称
	 * */
	public static String keyNickName;

	/**
	 * Letv.propertise 文件内容集合，无Letv.parment数据
	 * */
	public static ArrayList<String> PropertiseList;

	/**
	 * 所以需要打包的渠道号集合
	 * */
	public static ArrayList<String> Partners;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * 解析HalfAI.ini 文件对象
		 * */
		PullHalfAI pullHalfAI = new PullHalfAI();

		/**
		 * 解析properties_config.xml文件对象
		 * */
		PullPartners pullPartners = new PullPartners();

		System.out.println("正在加载工作空间......");
		MyTools.loadWorkSpaces();

		System.out.println("清空Temp文件夹数据......");
		MyTools.cleanTemp(HalfAI.Create_APK_in_Half_AI_Path + "\\temp");

		System.out.println("清空上次生成的APK......");
		Scanner sc = new Scanner(System.in);
		System.out.println("是否清空上次生成的APK文件，\n输入 y ：清空；\n输入 n ：不清空");
		if (sc.nextLine().equals("y")) {
			MyTools.cleanTemp(HalfAI.Create_APK_in_Half_AI_Path + "\\signerAPK");
		}
		
		// 如果地址有误，则不向下执行
		if (!MyTools.check_Path(HALFAI_INI_PATH)) {
			return;
		}

		/**
		 * 读取HalfAI.json文件，转换成流
		 * */
		InputStream is = MyTools.changeToInputStream(HALFAI_INI_PATH);

		try {
			pullHalfAI.parserData(is);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Main Error " + e.toString());
		}

		// TODO LHY 添加解析AndroidTools目录功能
		new MyTools().copyApkBuilder();

		/**
		 * 加载Letv.propertise 文件,无Letv.partent一行
		 * */
		PropertiseList = MyTools.LoadLetvPreperties(LetvProperties_path);

		/**
		 * 获得partnersList流
		 * */
		InputStream isPartners = MyTools.changeToInputStream(propConfigPath);

		// 解析partnersList流
		try {
			pullPartners.parserData(isPartners);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error pullPartners.parserData(isPartners); = " + e.toString());
		}

		// 加载Partner数据
		Partners = pullPartners.getList();
		for (String values : Partners) {
			MyTools.createTempPropties(PropertiseList, values);
		}

		// 比对APK是否生成成功
		MyTools.comparAPK();
	}

}
