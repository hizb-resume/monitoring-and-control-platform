package com.plantapp1_0_0;

public class MyConnect {
	static String ip="http://115.159.89.24:8080/";
	static String[] greenhouses=null;//���������б�����Դ
	static int[] shedId=new int[1];//���������б�ÿ���������ڷ�������id
	static int lastOkValue[];// ���ƽ���ÿ���豸��һ�����óɹ���ֵ������������ʧ�ܻ����ʧ��ʱ��������
	
/*	String address;
	static String data;
	
	String url_que = "http://112.24.25.130:8080/environment/socket/login?username=";
	static int connect_sign = 1;// �������ӳɹ���
	Thread readURL;
	URL url;
	URLConnection myConn;
	InputStream in;
	
	public MyConnect() {
		address = null;
		url = null;
		data = "";
	}

	public void setAd(String a) {
		address = a;
		try {
			// url=new URL(address);
			url = new URL(
					"http://112.24.25.130:8080/environment/socket/login?username=troy&password=1314520");
			try {
				myConn = url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int login(String id, String psw) {
		String a = url_que + id + "&password=" + psw;
		setAd(a);
		send();
		
		//Myconnect();

		JSONObject json = JSON.parseObject(data);
		Map map = (Map) JSON.toJSON(json);
		System.out.println(data);
		// String msg=(String) map.get("msg");
		String msg = "1001";
		if (msg.equals("1001"))// �ɹ�
		{
			return 1;
		} else if (msg.equals("1000"))// �޴��û�
			return 0;
		else if (msg.equals("1002"))// �������
			return -1;
		else
			// ����
			return -2;

	}

	public void ctrSend(int spinner_sel, int i, int value) {
		// ���Ϳ����ź�
		// spinner_sel�������б���룬��0��ʼ��,
		// int i ��i���豸����0��ʼ��,
		// int value �豸���Σ�-2~2

	}

	public void ctrRecv(int spinner_sel, int value[]) {
		// spinner_sel�������б���룬��0��ʼ��,
		// value[0~3]:�����ĸ��豸�ĵ���
		// example: value[2]=2;

	}

	public void Myconnect() {
		try {
			url = new URL(
					"http://112.24.25.130:8080/environment/socket/login?username=troy&password=1314520");
			in = url.openStream();

			byte[] b = new byte[1024];
			int n = -1;

			while ((n = in.read(b)) != -1) {
				String str = new String(b, 0, n);
				System.out.println(str);
				data=str;
				// data+=str;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send() {

		try {
			look = new Look();
			look.setURL(url);
			readURL = new Thread(look);
			readURL.start();
			data = look.data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data = "err";
		}
		
		 * try {
		 * 
		 * in=myConn.getInputStream(); BufferedInputStream bis = new
		 * BufferedInputStream(in);//��ȡBufferedInputStream���� ByteArrayBuffer baf
		 * = new ByteArrayBuffer(bis.available()); int ttt = 0; while((ttt =
		 * bis.read())!= -1){ //��ȡBufferedInputStream������ baf.append((byte)ttt);
		 * //�����ݶ�ȡ��ByteArrayBuffer�� } data =
		 * EncodingUtils.getString(baf.toByteArray(), "UTF-8"); //ת��Ϊ�ַ���
		 * 
		 * byte []b=new byte[1024];
		 * 
		 * int n = in.read(b) ;
		 * 
		 * data = new String(b,0,n) ; // int n=-1; // while((n=in.read(b))!=-1){
		 * // String str=new String(b,0,n); // data+=str; // }
		 * 
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); connect_sign=0; }
		 
	}*/

}
