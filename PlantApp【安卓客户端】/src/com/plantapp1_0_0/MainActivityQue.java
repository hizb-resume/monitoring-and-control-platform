package com.plantapp1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*import java.util.ArrayList;
 import java.util.List;*/
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.refen.Corp;
/*import com.refen.Shed;*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityQue extends Activity implements View.OnClickListener,
		OnItemSelectedListener {
//	MyConnect con;

	private long exitTime = 0;//���ڼ����û����������η��ؼ���ʱ�����������Ƿ��˳�����
	private Spinner spinner_greenhouses;// ѡ�����ҵ������б�
	private TextView txtSun;// �������ֵ�ؼ�
	private TextView txtTemIn;// �����¶�ֵ
	private TextView txtWet;// ����ʪ��ֵ
	private TextView txtTemOut;// �������ֵ
	private TextView txtCO2;// �������ֵ
	
	String light1;//����ӷ�������ȡ�Ĺ���ֵ
	String temp1;//�����¶�
	String humi1;//ʪ��
	String outtemp1;//�����¶�
	String gas1;//CO2Ũ��
	

	
	List<String> cropId;//����ӷ�������ȡ����������ID
	List<String> cropCode;//����ӷ�������ȡ�������������
	List<String> cropName;//����ӷ�������ȡ������������
	
/*	List<String> cropId=new ArrayList<String>();
	List<String> cropCode=new ArrayList<String>();
	List<String> cropName=new ArrayList<String>();
*/
	/*
	 * private TextView txtSun2;// ���ڹ���ֵ private TextView txtTem2;// �����¶�ֵ
	 * private TextView txtWet2;// ����ʪ��ֵ
	 */
	// private String str_Sun = "102300";// �������ֵ
	// private String str_TemIn = "31";// �����¶�ֵ
	// private String str_Wet = "78";// ����ʪ��ֵ
	// private String str_TemOut = "3.6";// �������ֵ
	// private String str_CO2 = "66";// �������ֵ

	/*
	 * private String str_Sun2 = "98345";// ���ڹ���ֵ private String str_Tem2 =
	 * "36";// �����¶�ֵ private String str_Wet2 = "82";// ����ʪ��ֵ
	 */
	private int spinner_sel = 0;// ��ǰѡ��������������б�����

	// private DictDaoImpl dao = null;
	private TableLayout table = null;//����������

	// private int orders = 0; // �û���¼����ordersֵ
	// int i = 0;

	String url_que = MyConnect.ip + "/environment/socket/shed/get?shedId=";
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
			String ip = url_que + MyConnect.shedId[spinner_sel];
			// String ip="http://www.baidu.com";
			String data = Myconnect(ip);
			ret = que(data);
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

	public int que(String data) {
		//���ݷ��������ص����ݣ���ȡ���Ƿ��ѯ�ɹ�����Ϣ�������ѯ�ɹ�������ȡ���������ݺ���������
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
		
		
		String msg = (String) map.get("msg");
		if (msg.equals("suc")) {

			// int n=(int)map.get("number");//��������
			/*
			 * int n=0; String codes[]=new String[n]; int shedIds[]=new int[n];
			 */
		
		light1=(String) map.get("light");
		temp1=(String) map.get("temp");
		humi1=(String) map.get("humi");
		outtemp1=(String) map.get("outtemp");
		gas1=(String) map.get("gas");
//			txtSun.setText((String) map.get("light"));
//			txtTemIn.setText((String) map.get("temp"));
//			txtWet.setText((String) map.get("humi"));
//			txtTemOut.setText((String) map.get("outtemp"));
//			txtCO2.setText((String) map.get("gas"));
		//	String a = "0", b = "0", c = "0";// a����Id b������� c������
			JSONArray arr = (JSONArray) map.get("corps");
			
			for (Object object : arr) {
				JSONObject shedjson = (JSONObject) object;
				Corp corp = JSON.toJavaObject(shedjson, Corp.class);
				cropId=new ArrayList<String>();
				cropCode=new ArrayList<String>();
				cropName=new ArrayList<String>();
				
				cropId.add(corp.getCode());
				cropCode.add(corp.getId() + "");
				cropName.add(corp.getName());
				
				/*
				 * codes[i]=shed.getCode(); shedIds[i]=shed.getId();
				 */
				// i++;

			}
			return 1;
		} else
			return -1;
	}

	public void initTable() {//��ʼ����񲼾�
		table.removeAllViews();
		addTableRow("����id", "�������", "������", 1);
		/*
		 * addTableRow("1", "XLH", "������", 0); addTableRow("2", "BC", "����", 0);
		 * addTableRow("3", "XHS", "������", 0); addTableRow("4", "PT", "����", 0);
		 */
		// addTableRow("5", "PGS", "ƻ����", 0);

		/*
		 * for(int j=0;j<=20;j++){ addTableRow(j+"", "�������", "������"); }
		 */

	}

	public void addTableRow(String a, String b, String c, int tit) {
		//���һ����������
		// a����Id b������� c������ tit��ʾ���������ǲ��Ǳ�ͷ
		// table.removeAllViews();
		TableRow row1 = new TableRow(this);
		TextView txt[] = new TextView[3];
		for (int i = 0; i < 3; i++) {
			txt[i] = new TextView(this);
			if (i == 0)
				txt[i].setText(a);
			if (i == 1)
				txt[i].setText(b);
			if (i == 2)
				txt[i].setText(c);

			txt[i].setPadding(3, 3, 3, 3);
			txt[i].setVisibility(View.VISIBLE);
			txt[i].setGravity(Gravity.CENTER);
			txt[i].setTextSize(22);
			txt[i].setBackgroundDrawable(getResources().getDrawable(
					R.drawable.table_frame_gray));
			if (tit == 1) {
				txt[i].setTextColor(0xffB23AEE);
			} else
				txt[i].setTextColor(0xfff37301);
			row1.addView(txt[i]);
		}

		table.addView(row1);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_que);
		SysApplication.getInstance().addActivity(this);
//		con = new MyConnect();
		txtSun = (TextView) findViewById(R.id.txtSun);
		txtTemIn = (TextView) findViewById(R.id.txtTemIn);
		txtWet = (TextView) findViewById(R.id.txtWet);
		txtTemOut = (TextView) findViewById(R.id.txtTemOut);
		txtCO2 = (TextView) findViewById(R.id.txtCO2);

		//

		/*
		 * txtSun2 = (TextView) findViewById(R.id.txtSun2); txtTem2 = (TextView)
		 * findViewById(R.id.txtTem2); txtWet2 = (TextView)
		 * findViewById(R.id.txtWet2);
		 */

		Button btnQue = (Button) findViewById(R.id.btnQue);
		btnQue.setOnClickListener(this);

		Button btnCtr = (Button) findViewById(R.id.btnCtr);
		btnCtr.setOnClickListener(this);

		Button btnSet = (Button) findViewById(R.id.btnSet);
		btnSet.setOnClickListener(this);

		Button btnHis = (Button) findViewById(R.id.btnHis);
		btnHis.setOnClickListener(this);

		Button refresh_greenhouse = (Button) findViewById(R.id.refresh_greenhouse);
		refresh_greenhouse.setOnClickListener(this);
		spinner_greenhouses = (Spinner) findViewById(R.id.spinner_greenhouse);
		/*
		 * ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		 * this, R.array.greenhouses, R.layout.my_simple_spinner_item);
		 */
		if (MyConnect.greenhouses == null) {
			MyConnect.greenhouses = new String[1];
			MyConnect.greenhouses[0] = "��û������";
		}
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.my_simple_spinner_item, MyConnect.greenhouses);
		// ArrayAdapter<CharSequence> adapter =new
		// ArrayAdapter<CharSequence>(this,R.layout.my_simple_spinner_item,MyConnect.greenhouses);
		adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
		spinner_greenhouses.setAdapter(adapter);
		spinner_greenhouses.setPrompt("��ѡ�����ң�");
		spinner_greenhouses.setSelection(0, true);
		spinner_greenhouses.setOnItemSelectedListener(this);

		table = (TableLayout) findViewById(R.id.dictTable);
		initTable();
		addTableRow("0", "0", "0", 0);

		freshData();
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
			Intent intent = new Intent(MainActivityQue.this,
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
		case R.id.refresh_greenhouse: {
			freshData();

		}
			break;
		case R.id.btnQue: {
			// �����ѯ����¼�
			// Intent intent = new Intent(MainActivityQue.this,
			// MainActivityQue.class);
			// startActivity(intent);
		}
			break;
		case R.id.btnCtr: {
			// ������ƺ���¼�
			Intent intent = new Intent(MainActivityQue.this,
					MainActivityCtr.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnSet: {
			// ������ú���¼�
			Intent intent = new Intent(MainActivityQue.this,
					MainActivitySet.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnHis: {
			// �����ʷ����¼�
			Intent intent = new Intent(MainActivityQue.this,
					MainActivityHis.class);
			startActivity(intent);
			finish();
		}
			break;
		}
	}

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
		//ˢ��ҳ�����ݣ�����ʼ��ҳ�桢ѡ�������б����ˢ�°�ťʱ���ã��ӷ������������ݼ���ش���

		// ��ǰѡ�������Id �� spinner_sel

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
			Toast toast = Toast.makeText(getApplicationContext(), "ˢ�³ɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			

			initTable();
			txtSun.setText(light1);
			txtTemIn.setText(temp1);
			txtWet.setText(humi1);
			txtTemOut.setText(outtemp1);
			txtCO2.setText(gas1);
			
			for(int i=0;i<cropId.size();i++){
				addTableRow(cropId.get(i), cropCode.get(i), cropName.get(i), 0);
			}

			
		} else {
			String err_msg="���ӷ�������ʱ��";
			if(ret==-1)
				err_msg="�ô���û�����ݣ�";
			Toast toast = Toast.makeText(getApplicationContext(), err_msg,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

			txtSun.setText("0");
			txtTemIn.setText("0");
			txtWet.setText("0");
			txtTemOut.setText("0");
			txtCO2.setText("0");
			table = (TableLayout) findViewById(R.id.dictTable);
			initTable();
			addTableRow("0", "0", "0", 0);
		}

		/*
		 * txtSun2.setText(str_Sun2); txtTem2.setText(str_Tem2);
		 * txtWet2.setText(str_Wet2);
		 */
	}

}
