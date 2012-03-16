import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;

public class MyTools {

	/**
	 * 比对打包失败的APK
	 * */
	public static void comparAPK() {
		System.out.println("=================================================================");
		ArrayList<String> errAPK = new ArrayList<String>();
		ArrayList<String> filesList = new ArrayList<String>();
		File file = new File(HalfAI.Create_APK_in_Half_AI_Path + "\\signerAPK\\");
		System.out.println(file.getAbsolutePath());
		String[] files = file.list();

		// 将文件目录列表数组转换成集合
		for (String s : files) {
			filesList.add(s);
		}

		JSONArray jsonArray = new JSONArray();
		// 判断是否打包成功
		for (String s : HalfAI.Partners) {
			if (!filesList.contains(HalfAI.ApkName + "_" + s + ".apk")) {
				errAPK.add(s);
				System.out.println("渠道号" + s + "打包失败");
				jsonArray.put(s);
			}
		}
		if (errAPK.size() != 0) {
			System.out.println("JSONArray = " + jsonArray.toString());
		}
		System.out.println("=================================================================");
		showLog(errAPK);
	}

	
	public static void showLog(ArrayList <String> err){
		ArrayList <String>list = new ArrayList<String>();
		JSONArray jsArray = new JSONArray();
		list.add(HalfAI.ApkName + "项目自动打包结果：\n");
		list.add("	");
		list.add("	共打包 " + HalfAI.Partners.size() + " 个APK\n");
		list.add("	");
		list.add("	失败 " + err.size() + "个\n");
		list.add("	");
		for(String s : err){
			list.add("	" + s + "			失败");
			list.add("	");
			jsArray.put(s);
		}
		list.add("	");
		list.add("	");
		list.add("	" + jsArray.toString() + "\n\n\n");
		list.add("	");
		list.add("	");
		list.add("	");
		list.add("	");
		list.add("===>===>===>===>===>===>===>===>===>===>===>===>===>===>===>");
		list.add("");
		String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		list.add(s);
		list.add("Author:LiuHeyuan");
		
		
		//复制文件
		FileWriter fw = null;
		BufferedWriter buffw = null;
		PrintWriter pw = null;

		File logFile = new File(HalfAI.Create_APK_in_Half_AI_Path + "\\log.txt");

		if (logFile.exists()) {
			logFile.delete();
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			fw = new FileWriter(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffw = new BufferedWriter(fw);
		pw = new PrintWriter(buffw);

		for (String tempValue : list) {

			pw.println(tempValue);
		}
		list.clear();
		list = null;
		pw.flush();
		pw.close();
		try {
			buffw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//打开文件
		try {
			Runtime.getRuntime().exec("cmd /c start notepad " + logFile.getAbsolutePath() + "&exit");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 格式化工作空间目录
	 * */
	public static String formatPath(String s) {
		StringBuilder sb = new StringBuilder();
		String[] strAll = s.split("\\\\");
		for (int i = 0; i < strAll.length; i++) {
			String tmp = strAll[i];
			if (tmp.indexOf(" ") != -1) {
				strAll[i] = changeStrSpace(tmp);
			}
			if (i == 0) {
				sb.append(strAll[i]);
			} else {
				sb.append("\\" + strAll[i]);
			}

		}
		return sb.toString();
	}

	public static String changeStrSpace(String s) {
		int num = 6;
		String tmp = s;
		int i = s.indexOf(" ");
		System.out.println("I=" + i);
		if (i == 6) {
			tmp = s.split(" ")[0].toUpperCase() + "~1";
		} else if (i < 6) {
			tmp = s.split(" ")[0].toUpperCase() + s.split(" ")[1].substring(0, num - i).toUpperCase() + "~1";
		} else if (i > 6) {
			tmp = s.split(" ")[0].toUpperCase().substring(0, 6) + "~1";
		}

		return tmp;

	}

	/**
	 * 加载工作空间
	 * */
	public static void loadWorkSpaces() {
		String fs = System.getProperty("user.dir");
		HalfAI.Create_APK_in_Half_AI_Path = formatPath(fs);
		System.out.println("加载目录：" + HalfAI.Create_APK_in_Half_AI_Path);

		HalfAI.HALFAI_INI_PATH = HalfAI.Create_APK_in_Half_AI_Path + "\\HalfAI.json";
		System.out.println("加载HalfAI.json文件：" + HalfAI.HALFAI_INI_PATH);

		HalfAI.LetvProperties_path = HalfAI.Create_APK_in_Half_AI_Path + "\\properties_files\\letv.properties";
		System.out.println("加载letv.properties文件：" + HalfAI.LetvProperties_path);

		HalfAI.propConfigPath = HalfAI.Create_APK_in_Half_AI_Path + "\\properties_config.json";
		System.out.println("加载properties_config.json文件：" + HalfAI.propConfigPath);
	}

	/**
	 * 复制ApkBuilder.bat
	 * */
	public void copyApkBuilder() {
		HalfAI.ANDROID_TOOLS_PATH = HalfAI.apkbuilder_path.substring(0, HalfAI.apkbuilder_path.lastIndexOf("\\"));
		// 复制apkbuliderforhalfai.bat到android tools 目录
		String sourceAPKbuilder = HalfAI.Create_APK_in_Half_AI_Path + "\\bin\\apkbuilderforhalfai.bat";
		String toApkToolsPath = HalfAI.ANDROID_TOOLS_PATH + "\\apkbuilderforhalfai.bat";
		HalfAI.apkbuilder_path = toApkToolsPath;

		ArrayList<String> list = new ArrayList<String>();
		File file = new File(sourceAPKbuilder);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error LoadLetvPreperties " + e.toString());
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String strTemp = null;
		do {
			try {
				strTemp = buf.readLine();
				if (null == strTemp) {
					break;
				}
				list.add(strTemp);
				// FIXME 测试
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error LoadLetvPreperties do " + e.toString());
			}
		} while (strTemp != null);

		FileWriter fw = null;
		BufferedWriter buffw = null;
		PrintWriter pw = null;

		File toapkbuilderfile = new File(toApkToolsPath);

		if (toapkbuilderfile.exists()) {
			toapkbuilderfile.delete();
			try {
				toapkbuilderfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				toapkbuilderfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			fw = new FileWriter(toapkbuilderfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffw = new BufferedWriter(fw);
		pw = new PrintWriter(buffw);

		for (String tempValue : list) {

			pw.println(tempValue);
		}
		pw.println("exit");
		list.clear();
		list = null;
		pw.flush();
		pw.close();
		try {
			buffw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 清空垃圾数据
	 * */
	public static void cleanTemp(String path) {
		// File file = new File(HalfAI.Create_APK_in_Half_AI_Path + "\\temp");
		File file = new File(path);
		File files[] = file.listFiles();
		for (File f : files) {
			System.out.println("Delete>>>>>>" + f.getAbsolutePath());
			if (f.isDirectory()) {
				cleanTemp(f.getAbsolutePath());
			}
			f.delete();
		}
	}

	/**
	 * 创建一个临时的Letv.Proptie的内容 lists为原letv.propties文件内容，values为partenrs的值
	 * */
	public static void createTempPropties(ArrayList<String> lists, String values) {
		ArrayList<String> list = new ArrayList<String>();

		for (String s : lists) {
			list.add(s);
		}

		list.add(HalfAI.PARTNERKEY + values);

		FileWriter fw = null;
		BufferedWriter buffw = null;
		PrintWriter pw = null;

		File Letv_properties = new File(HalfAI.Create_APK_in_Half_AI_Path + "\\properties_files\\letv.properties");

		if (Letv_properties.exists()) {
			Letv_properties.delete();
			try {
				Letv_properties.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Letv_properties.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			fw = new FileWriter(Letv_properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buffw = new BufferedWriter(fw);
		pw = new PrintWriter(buffw);

		for (String tempValue : list) {

			pw.println(tempValue);
		}
		list.clear();
		list = null;
		pw.flush();
		pw.close();
		try {
			buffw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			createUnSingerAPK(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成未签名的打包文件
	 * */
	public static void createUnSingerAPK(String partenvalues) throws Exception {
		String cmdHead = "cmd /c start ";
		// String cmdHead = "cmd /c ";
		String cmdEnter = "\n";
//		String pass = "dkeu*&kdue";
		//看音乐Pass=3h3jf!@jfdsj
		String storePassKey = "-storepass";
		String end = " & exit";
		String putPass = "-storepass " + HalfAI.keypass + " -keypass " + HalfAI.keypass + " ";
		String packageUnSingerCMD = HalfAI.apkbuilder_path + " " + HalfAI.Create_APK_in_Half_AI_Path + "\\temp\\unsigner_" + partenvalues + ".apk -v -u -z " + HalfAI.Create_APK_in_Half_AI_Path + "\\bin\\resources.ap_ -f " + HalfAI.Create_APK_in_Half_AI_Path + "\\bin\\classes.dex -rf " + HalfAI.Create_APK_in_Half_AI_Path + "\\properties_files";
		Process unSinger = Runtime.getRuntime().exec(cmdHead + packageUnSingerCMD);
		// Runtime.getRuntime().exec(cmdHead + packageUnSingerCMD);

		System.out.println("本次执行打包未签名APK命令：" + packageUnSingerCMD);
		System.out.println("等待打包未签名APK完成后，按'f'并回车");
		while (true) {
			String okun = new Scanner(System.in).nextLine();
			if (null != okun && okun.equals("f")) {
				unSinger.destroy();
				break;
			} else if (null != okun && okun.equals("exit")) {
				unSinger.destroy();
				System.exit(0);
				break;
			}
		}
		String jarsignerCMD = "jarsigner -verbose -keystore " + HalfAI.Create_APK_in_Half_AI_Path + "\\" + HalfAI.keyfilename + " " + putPass + "-signedjar " + HalfAI.Create_APK_in_Half_AI_Path + "\\signerAPK\\" + HalfAI.ApkName + "_" + partenvalues + ".apk " + HalfAI.Create_APK_in_Half_AI_Path + "\\temp\\unsigner_" + partenvalues + ".apk " + HalfAI.keyNickName;
		// Runtime.getRuntime().exec(cmdHead + jarsignerCMD);
		Process singerProcess = Runtime.getRuntime().exec(cmdHead + jarsignerCMD);
		System.out.println("本次执行签名APK命令：" + jarsignerCMD);
		System.out.println("等待打包完成后，按'f'并回车");
		while (true) {
			String oksigner = new Scanner(System.in).nextLine();
			if (null != oksigner && oksigner.equals("f")) {
				singerProcess.destroy();
				break;
			} else if (null != oksigner && oksigner.equals("exit")) {
				unSinger.destroy();
				System.exit(0);
				break;
			}
		}
	}

	/**
	 * 将文件转换为流
	 * */
	public static InputStream changeToInputStream(String fileName) {
		File f = new File(fileName);
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			return is;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断文件绝对路径是否有误
	 * */
	public static boolean check_Path(String path) {
		if (null == path || "".equals(path.trim())) {
			System.out.println("您输入的位置有误！！！");
			return false;
		}
		HalfAI.HALFAI_INI_PATH = path.replace("\"", "").trim();
		return true;
	}

	// /**
	// * 从pullHalfAIMap中获得数据
	// * */
	// public static void getHalfAIMap(HashMap<String, String> pullHalfAIMap) {
	// HalfAI.apkbuilder_path = pullHalfAIMap.get(PullHalfAI.APKBUILDER_PATH);
	// HalfAI.Create_APK_in_Half_AI_Path =
	// pullHalfAIMap.get(PullHalfAI.CREATE_APK_IN_HALF_AI_PATH);
	// HalfAI.LetvProperties_path =
	// pullHalfAIMap.get(PullHalfAI.LETVPROPERTIES_PATH);
	// HalfAI.ApkName = pullHalfAIMap.get(PullHalfAI.APKNAME);
	// }

	// /**
	// * 加载partners数据
	// * */
	// public static ArrayList<String> LoadLetvPartners(String path){
	// ArrayList<String> list = new ArrayList<String> ();
	// File file = new File(path);
	// InputStream is = null;
	// try {
	// is = new FileInputStream(file);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// System.out.println("Error LoadLetvPreperties "+e.toString());
	// }
	// BufferedReader buf = new BufferedReader(new InputStreamReader(is));
	// String strTemp = null;
	// do{
	// try {
	// strTemp = buf.readLine();
	// if(null == strTemp){
	// break;
	// }
	// if(strTemp.startsWith("letv.partner=")){
	// continue;
	// }
	// list.add(strTemp);
	// //FIXME 测试
	// System.out.println("测试-打印Letv.propertise = " + strTemp);
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.out.println("Error LoadLetvPreperties do "+e.toString());
	// }
	// }while(strTemp != null);
	//
	// return list;
	// }

	/**
	 * 加载Letv.preperties文件到ArrayList中 注意：并没有将letv.partner=这一行加入其中
	 * */
	public static ArrayList<String> LoadLetvPreperties(String path) {
		ArrayList<String> list = new ArrayList<String>();
		File file = new File(path);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error LoadLetvPreperties " + e.toString());
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String strTemp = null;
		do {
			try {
				strTemp = buf.readLine();
				if (null == strTemp) {
					break;
				}
				if (strTemp.startsWith("letv.partner=")) {
					continue;
				}
				list.add(strTemp);
				// FIXME 测试
				System.out.println("测试-打印Letv.propertise = " + strTemp);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error LoadLetvPreperties do " + e.toString());
			}
		} while (strTemp != null);

		return list;
	}
}
