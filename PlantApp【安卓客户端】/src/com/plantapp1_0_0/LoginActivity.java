package com.plantapp1_0_0;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.refen.Shed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {

	public static final String SP_INFOS = "SPDATA_Files";
	public static final String USERID = "UserID";
	public static final String PASSWORD = "PassWord";
//	MyConnect con;
	// String url="http://112.24.27.74:8080/environment/socket/login?username=";
	

	private static String uidstr; // �û��ʺ�,��ס������
	private static String pwdstr; // �û�����
	
	String idd;//��¼��
	String psww;

	private EditText id;//�����û����Ŀؼ�
	private EditText psw;//��������Ŀؼ�
	private TextView loginSucTip;//��¼ʧ�ܵĸ���ԭ����ʾ
	private long exitTime = 0;//���ڼ����û����������η��ؼ���ʱ�����������Ƿ��˳�����
	private static CheckBox cb; // "��ס��"��ѡ�����

	private static boolean key_correct;//�Ƿ񱣴�����ı�ǣ�ֻ�е��û�����������ȷ�ű���

	/*
	 * String data1; int t;
	 */
	
	
	String url_que = MyConnect.ip + "/environment/socket/login?username=";
	int t;//�жϺͷ������Ƿ�ͨѶ���
	int ret;//�����ź�

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("value");
			Log.i("mylog", "������Ϊ-->" + val);
			// TODO
			// UI����ĸ��µ���ز���
		}
	};

	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// TODO
			// ��������� http request.����������ز���

			String ip =url_que + idd + "&password=" + psww;
			String data=Myconnect(ip);
			ret=login(data);
			t=1;

			Message msg = new Message();
			Bundle data2 = new Bundle();
			data2.putString("value", "������");
			msg.setData(data2);
			handler.sendMessage(msg);
		}
	};

	public String Myconnect(String ip) {//ip:ͨ�ŵ�url��ַ
		try {
			String data="";
			URL url = new URL(ip);
			InputStream in = url.openStream();
			byte[] b = new byte[1024];
			int n = -1;
			while ((n = in.read(b)) != -1) {
				String str = new String(b, 0, n);
				data += str;
			}
			return data;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}



	public int login(String data) {	
		//���ݷ��������ص����ݣ���ȡ����¼�Ƿ�ɹ�����Ϣ�������¼�ɹ�������ȡ���û��Ĵ�����Ϣ
		//����data:�ӷ�������ȡ��������	
		if(data==null||data.equals(""))
			return -2;
		JSONObject json ;
		try {
			json = JSON.parseObject(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return -2;
		}
		Map map = (Map) JSON.toJSON(json);
		System.out.println(data);
		 String msg=(String) map.get("msg");
		// int n=(int)map.get("number");//��������
		 int n=0; 
		if (msg.equals("1001"))// �ɹ�
		{
			List<String> dapeng=new ArrayList<String>();
			 List<Integer> dapengId=new ArrayList<Integer>();
			 JSONArray arr=(JSONArray) map.get("sheds");
			 for(Object object:arr){
				 JSONObject shedjson=(JSONObject) object;
				 Shed shed=JSON.toJavaObject(shedjson, Shed.class);
				 dapeng.add(shed.getCode());
				 dapengId.add(shed.getId());
				 /*shedIds[i]=shed.getId();*/
				 n++;
				 
			 }
			 String codes[]=new String[n];
			 int shedIds[]=new int[n];
			 for(int i=0;i<n;i++){
				 codes[i]=dapeng.get(i);
				 shedIds[i]=dapengId.get(i);
			 }
			MyConnect.greenhouses=codes;
			MyConnect.shedId=shedIds;
			return 1;
		} else if (msg.equals("1000"))// �޴��û�
			return 0;
		else if (msg.equals("1002"))// �������
			return -1;
		else
			// ����
			return -2;

	}
	
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {//����Activityǰ��һЩ׼��������ȡ��ҳ�沼���еĿؼ�����ʼ����ر���
		//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SysApplication.getInstance().addActivity(this);

		id = (EditText) findViewById(R.id.loginid);
		psw = (EditText) findViewById(R.id.loginpsw);
		loginSucTip = (TextView) findViewById(R.id.LoginSucTip);

//		con = new MyConnect();

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		Button btnReg = (Button) findViewById(R.id.btnReg);
		btnReg.setOnClickListener(this);

		key_correct = false;

		cb = (CheckBox) findViewById(R.id.cbRemember); // ���CheckBox����
		checkIfRemember(); // ��SharedPreferences�ж�ȡ�û����ʺź�����

	}

	@Override
	protected void onStop() {//����û�����˼�ס���벢�ҵ�¼�ɹ������û���Ϣ����
		super.onStop();
		if (cb.isChecked() && (key_correct)) {
			uidstr = id.getText().toString().trim(); // ���������ʺ�
			pwdstr = psw.getText().toString().trim(); // ������������
			rememberMe(uidstr, pwdstr); // ���û����ʺ����������SharedPreferences
		} else {
			rememberMe(null, null);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// ��������SharedPreferences�ж�ȡ�û����ʺź�����
	public void checkIfRemember() {
		SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
		// ���Preferences
		uidstr = sp.getString(USERID, null); // ȡPreferences�е��ʺ�
		pwdstr = sp.getString(PASSWORD, null); // ȡPreferences�е�����
		if (uidstr != null && pwdstr != null) {
			id.setText(uidstr); // ��EditText�ؼ����ʺ�
			psw.setText(pwdstr); // ��EditText�ؼ�������
			cb.setChecked(true);
		}
	}

	// ���������û���id���������SharedPreferences
	public void rememberMe(String uid, String pwd) {
		SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
		// ���Preferences
		SharedPreferences.Editor editor = sp.edit(); // ���Editor
		editor.putString(USERID, uid); // ���û����ʺŴ���Preferences
		editor.putString(PASSWORD, pwd); // ���������Preferences
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//����˳������ѡ��˵�
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		menu.add(0, 0, 0, R.string.exit).setIcon(R.drawable.exitt);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) { // �жϰ��µĲ˵�ѡ��
		case 0: // �����ˡ��˳����˵�
			SysApplication.getInstance().exit();// �˳�����
			// System.exit(0);//�˳�����
			// pos =
			// DiaryListActivity.this.lv.getSelectedItemPosition();//ȡListView��ǰ���ID
			// showDialog(DIALOG_DELETE); //��ʾȷ��ɾ���Ի��򣬲�ʵʩ��Ӧ����
			break;
		}
		return true;
		// return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {//���������ť�ļ����¼�
		//����v����ǰ����İ�ť
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin: {
			// �����¼����¼�
			idd = id.getEditableText().toString().trim();
			psww  = psw.getEditableText().toString().trim();

			if (idd.equals("")) {
				loginSucTip.setText("�������û�����");
			} else if (psww.equals("")) {
				loginSucTip.setText("���������룡");
			} else {				
				t = 0;
				new Thread(networkTask).start();				
				long begin=System.currentTimeMillis();
				while (t == 0) {
					long end=System.currentTimeMillis();
					if((end-begin)>3000){
						ret=-2;
						break;
					}						
				}

				if (ret == 1) {// ��¼�ɹ�

					// loginSucTip.setText("��¼�ɹ�");

					key_correct = true;

					TextView tv = new TextView(this);
					tv.setText("��¼�ɹ���"); // ����
					//tv.setText(data1);
					tv.setTextSize(30);// �����С
					// tv.setPadding(30, 20, 10, 10);//λ��
					tv.setTextColor(Color.parseColor("#FF0000"));// ��ɫ
					tv.setGravity(Gravity.CENTER);
					tv.setWidth(200);
					tv.setHeight(100);
					// AlertDialog.Builder builder = new
					// AlertDialog.Builder(this).setTitle("��¼�ɹ�");
					AlertDialog.Builder builder = new AlertDialog.Builder(this)
							.setCustomTitle(tv);
					builder.setPositiveButton("ȷ��", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(LoginActivity.this,
									MainActivityQue.class);
							startActivity(intent);
							finish();
						}
					}).show();

					/*
					 * new
					 * AlertDialog.Builder(this).setTitle("��¼�ɹ�").setPositiveButton
					 * ("ȷ��",new OnClicklistener() { public void
					 * onClick(DialogInterface dialog, int which) {
					 * show.setText("�����ˡ�ȷ������ť��"); } }).show();
					 */
					// Intent intent = new Intent(LoginActivity.this,
					// MainActivityQue.class);
					// startActivity(intent);

				} else if (ret == -1)// �������
				{
					loginSucTip.setText("�������");
				}

				else if (ret == 0) {// �޴��û�
					loginSucTip.setText("�޴��û���");
				} /*else if (MyConnect.connect_sign == 0) {// ��������ʧ��
					loginSucTip.setText("��������ʧ�ܣ�");
				}*/ else// ����������
				{
					loginSucTip.setText("���ӷ�������ʱ��");
				}

			}

		}
			break;
		case R.id.btnReg: {
			// ������ź���¼�
			//�ò���Ϊ����������ã����������ɺ�Ӧ�Ƴ�

			Intent intent = new Intent(LoginActivity.this,
					MainActivityQue.class);
			startActivity(intent);
			finish();

		}
			break;
		}
	}

	/*
	 * boolean IsLoginOk(String sid, String spsw) { // �ж��Ƿ��¼�ɹ� // δд���ͷ�����ͨѶ
	 * 
	 * int c=con.login(sid,spsw); if (c==1) return true; else if(c==-1) return
	 * false; else if(c==0) return false; else return false; }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//����û��������ٵ�������η��ؼ������˳�����
		//������keycode�������룻event���¼�
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
