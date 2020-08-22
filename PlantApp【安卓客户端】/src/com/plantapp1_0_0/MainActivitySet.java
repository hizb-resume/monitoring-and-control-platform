package com.plantapp1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.refen.Shed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivitySet extends Activity implements View.OnClickListener,
		OnItemSelectedListener, TextWatcher {
//	MyConnect con;
	private long exitTime = 0;//���ڼ����û����������η��ؼ���ʱ�����������Ƿ��˳�����
	private Spinner spinner_greenhouses;// ѡ�����ҵ������б�
	private int spinner_sel = 0;// ��ǰѡ��������������б�����

	private EditText tem;//ҳ���ϵ��ĸ�����ؼ�EditText
	private EditText wet;
	private EditText sun;
	private EditText wind;
	
	String msgSetTemp ;//�������յ�������źź󷵻ص���ʾ��Ϣ
	String msgSetWet ;
	String msgSetSun ;
	String msgSetCO2 ;
	

	int change = 0;//���û��������ʱ�ж����ݺ���һ���Ƿ��иı䣬�������û�з����仯��ֻ���û���ʾ���������������
	String s1, s2, s3, s4;//�����Ԥ��ֵ

	String url_que = MyConnect.ip + "/environment/socket/shed/set?shedId=";
	int t;// �жϺͷ������Ƿ�ͨѶ���
	int ret;// �����ź�

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
			// ����ID��shedId[spinner_sel]
			// �������ݲ�����s1,s2,s3,s4
			 String ip =url_que + MyConnect.shedId[spinner_sel]+"&temp="+s1+"&light="+s3+"&humi="+s2+"&gas="+s4;
			//String ip = "http://www.baidu.com";
			String data = Myconnect(ip);
			ret = set(data);
			t = 1;

			Message msg = new Message();
			Bundle data2 = new Bundle();
			data2.putString("value", "������");
			msg.setData(data2);
			handler.sendMessage(msg);
		}
	};

	public String Myconnect(String ip) {
		try {
			String data = "";
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

	public int set(String data) {
		//���ݷ��������ص����ݣ���ȡ���Ƿ񱣴�ɹ�����Ϣ���������������ص���Ϣ��ʾ����
		//����data:�ӷ�������ȡ��������
		if (data == null || data.equals(""))
			return -2;
		JSONObject json;
		try {
			json = JSON.parseObject(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return -2;
		}
		Map map = (Map) JSON.toJSON(json);
		// System.out.println(data);
		msgSetTemp = (String) map.get("temp");
		msgSetWet = (String) map.get("light");
		msgSetSun = (String) map.get("humi");
		msgSetCO2 = (String) map.get("gas");
		
		// int n=(int)map.get("number");//��������
		/*
		 * int n=0; String codes[]=new String[n]; int shedIds[]=new int[n];
		 */
		/*int i = 0;
		JSONArray arr = (JSONArray) map.get("sheds");
		for (Object object : arr) {
			JSONObject shedjson = (JSONObject) object;
			Shed shed = JSON.toJavaObject(shedjson, Shed.class);
			
			 * codes[i]=shed.getCode(); shedIds[i]=shed.getId();
			 
			i++;

		}*/

		return 1;
		/*
		 * if (msg.equals("1001"))// �ɹ� { return 1; } else if
		 * (msg.equals("1000"))// �޴��û� return 0; else if (msg.equals("1002"))//
		 * ������� return -1; else // ���� return -2;
		 */

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_set);
		SysApplication.getInstance().addActivity(this);
//		con = new MyConnect();

		Button btnQue = (Button) findViewById(R.id.btnQue);
		btnQue.setOnClickListener(this);

		Button btnCtr = (Button) findViewById(R.id.btnCtr);
		btnCtr.setOnClickListener(this);

		Button btnSet = (Button) findViewById(R.id.btnSet);
		btnSet.setOnClickListener(this);

		Button btnHis = (Button) findViewById(R.id.btnHis);
		btnHis.setOnClickListener(this);

		spinner_greenhouses = (Spinner) findViewById(R.id.spinner_greenhouse);
		/*
		 * ArrayAdapter<CharSequence> adapter = ArrayAdapter
		 * .createFromResource(this, R.array.greenhouses,
		 * R.layout.my_simple_spinner_item);
		 */
		if (MyConnect.greenhouses == null) {
			MyConnect.greenhouses = new String[1];
			MyConnect.greenhouses[0] = "��û������";
		}
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.my_simple_spinner_item, MyConnect.greenhouses);
		adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
		spinner_greenhouses.setAdapter(adapter);
		spinner_greenhouses.setPrompt("��ѡ�����ң�");
		spinner_greenhouses.setSelection(0, true);
		spinner_greenhouses.setOnItemSelectedListener(this);

		Button refresh_greenhouse = (Button) findViewById(R.id.refresh_greenhouse);
		refresh_greenhouse.setOnClickListener(this);

		tem = (EditText) findViewById(R.id.set_tem);
		wet = (EditText) findViewById(R.id.set_wet);
		sun = (EditText) findViewById(R.id.set_sun);
		wind = (EditText) findViewById(R.id.set_wind);

		tem.addTextChangedListener(this);
		wet.addTextChangedListener(this);
		sun.addTextChangedListener(this);
		wind.addTextChangedListener(this);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);

		freshData();

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		/*
		 * change=1; Button t = (Button) findViewById(R.id.refresh_greenhouse);
		 * t.setText(change+"");
		 */
	}

	@Override
	public void afterTextChanged(Editable v) {
		// TODO Auto-generated method stub
		change = 1;
		// Button t = (Button) findViewById(R.id.refresh_greenhouse);
		// t.setText(change+"");

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		menu.add(0, 0, 0, R.string.logout).setIcon(R.drawable.logoutt);
		menu.add(0, 1, 0, R.string.exit).setIcon(R.drawable.exitt);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) { // �жϰ��µĲ˵�ѡ��
		case 0:// ע��
			Intent intent = new Intent(MainActivitySet.this,
					LoginActivity.class);
			startActivity(intent);
			finish();
		case 1: // �����ˡ��˳����˵�
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnQue: {
			// �����ѯ����¼�
			Intent intent = new Intent(MainActivitySet.this,
					MainActivityQue.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnCtr: {
			// ������ƺ���¼�
			Intent intent = new Intent(MainActivitySet.this,
					MainActivityCtr.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnSet: {
			// ������ú���¼�
			/*
			 * Intent intent = new Intent(MainActivityHis.this,
			 * MainActivitySet.class); startActivity(intent); finish();
			 */
		}
			break;
		case R.id.btnHis: {
			// �����ʷ����¼�
			Intent intent = new Intent(MainActivitySet.this,
					MainActivityHis.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.refresh_greenhouse: {
			// ���ˢ�º���¼�

			freshData();

		}
			break;
		case R.id.save: {

			// String sid = id.getEditableText().toString().trim();
			if (change == 0) {
				// ����û�и��Ĺ�

				Toast toast = Toast.makeText(getApplicationContext(),
						"����û�з����仯Ŷ��", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else {

				s1 = tem.getEditableText().toString().trim();
				s2 = wet.getEditableText().toString().trim();
				s3 = sun.getEditableText().toString().trim();
				s4 = wind.getEditableText().toString().trim();

				// ������ͳɹ���
				send();
				/*
				 * if (send(s1, s2, s3, s4)) { change = 0; Toast toast =
				 * Toast.makeText(getApplicationContext(), "����ɹ���",
				 * Toast.LENGTH_SHORT); toast.setGravity(Gravity.CENTER, 0, 0);
				 * toast.show();
				 * 
				 * }
				 */

			}
		}
			break;
		}
	}

	void send() {
		//����Ԥ�軷��ֵ������������ز���
		change = 0;
		t = 0;
		ret = 0;

		new Thread(networkTask).start();

		long begin = System.currentTimeMillis();

		while (t == 0) {
			long end = System.currentTimeMillis();
			if ((end - begin) > 3000) {
				ret = -2;
				break;
			}

		}
		


		if (ret == 1) {
			Toast toast = Toast.makeText(getApplicationContext(), msgSetTemp+"\n"+msgSetWet+"\n"+msgSetSun+"\n"+msgSetCO2,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "���ӷ�������ʱ��",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}

	}

	/*
	 * boolean send(String s1, String s2, String s3, String s4) { boolean a, b,
	 * c, d; a = b = c = d = true; if (!s1.equals("")) a = send(s1, 1); if
	 * (!s1.equals("")) b = send(s2, 2); if (!s1.equals("")) c = send(s3, 3); if
	 * (!s1.equals("")) d = send(s4, 4); return (a && b && c && d); }
	 * 
	 * boolean send(String s, int i) { // i����i��EditText // s����i��EditText��ֵ
	 * 
	 * // δд������������¼�����EditView��ֵ���͵������� return true;
	 * 
	 * // if����ʧ�ܣ�return false; }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		spinner_sel = arg2;
		/*
		 * Button t = (Button) findViewById(R.id.refresh_greenhouse);
		 * t.setText(spinner_sel+"");
		 */
		freshData();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	void freshData() {
		// �ӷ�������������s1~s4���ı�EditView��ֵ

		// ��ǰѡ�������Id �� spinner_sel

		// con.setRecv(spinner_sel,);

		String s1, s2, s3, s4;
		s1 = "";
		s2 = "";
		s3 = "";
		s4 = "";

		tem.setText(s1);
		wet.setText(s2);
		sun.setText(s3);
		wind.setText(s4);
		/*
		 * Toast toast = Toast.makeText(getApplicationContext(), "ˢ�³ɹ���",
		 * Toast.LENGTH_SHORT); toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show();
		 */
		change = 0;

		// 111
		/*
		 * Button t = (Button) findViewById(R.id.refresh_greenhouse);
		 * t.setText(change+"");
		 */

	}

}
