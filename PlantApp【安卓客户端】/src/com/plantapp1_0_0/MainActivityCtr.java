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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivityCtr extends Activity implements View.OnClickListener,
		OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
	// MyConnect con;
	private long exitTime = 0;//���ڼ����û����������η��ؼ���ʱ�����������Ƿ��˳�����
	private Spinner spinner_greenhouses;// ѡ�����ҵ������б�
	private int spinner_sel = 0;// ��ǰѡ��������������б�����
	private int eqpId;// ��ǰѡ����豸ID
	/*
	 * private ToggleButton togglebutton1; private ToggleButton togglebutton2;
	 * private ToggleButton togglebutton3; private ToggleButton togglebutton4;
	 */
	private SeekBar[] bar;//�ĸ��豸�ĵ��ο��ƿ���
	int value[];// ÿ���豸�ĵ�ǰ����
	
	private TextView tip_values[];//���û��϶�����ʱ��ʵʱ������ʾ��Ϣ

	/*
	 * private SeekBar[] bar1; private SeekBar bar2; private SeekBar bar3;
	 * private SeekBar bar4;
	 */

	// �豸�ĵ���-2~2
	private final int MAX = 2;
	private final int SEC_MAX = 1;
	private final int MIDDLE = 0;
	private final int SEC_MIN = -1;
	private final int MIN = -2;
	// private int value_temp;

	String url_que = MyConnect.ip + "/environment/socket/login?username=";//�˴���
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
			
			//��Ҫ�Ĳ�����
			// ����ID��shedId[spinner_sel]
			// �豸ieqpId(0~3),ֵΪvalue[ieqpId];
			
			 String ip =url_que + "";//�˴���
			
			String data = Myconnect(ip);
			ret = ctr(data);
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

	public int ctr(String data) {
		//���ݷ��������ص����ݣ���ȡ�������Ƿ�ɹ�����Ϣ
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
		String msg = (String) map.get("msg");

		if (msg.equals("suc")) {

			// ������ͳɹ���
			MyConnect.lastOkValue[eqpId] = value[eqpId];

			return 1;
		}
		else
			return -1;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_ctr);
		SysApplication.getInstance().addActivity(this);

		// con = new MyConnect();

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

		adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
		spinner_greenhouses.setAdapter(adapter);
		spinner_greenhouses.setPrompt("��ѡ�����ң�");
		spinner_greenhouses.setSelection(0, true);
		spinner_greenhouses.setOnItemSelectedListener(this);

		/*
		 * togglebutton1 = (ToggleButton) findViewById(R.id.togglebutton1);
		 * togglebutton2 = (ToggleButton) findViewById(R.id.togglebutton2);
		 * togglebutton3 = (ToggleButton) findViewById(R.id.togglebutton3);
		 * togglebutton4 = (ToggleButton) findViewById(R.id.togglebutton4);
		 * togglebutton1.setOnClickListener(this);
		 * togglebutton2.setOnClickListener(this);
		 * togglebutton3.setOnClickListener(this);
		 * togglebutton4.setOnClickListener(this);
		 */

		bar = new SeekBar[4];
		bar[0] = (SeekBar) findViewById(R.id.SeekBar1);
		bar[1] = (SeekBar) findViewById(R.id.SeekBar2);
		bar[2] = (SeekBar) findViewById(R.id.SeekBar3);
		bar[3] = (SeekBar) findViewById(R.id.SeekBar4);

		value = new int[4];
		MyConnect.lastOkValue = new int[4];

		tip_values = new TextView[4];
		tip_values[0] = (TextView) findViewById(R.id.vaule1);
		tip_values[1] = (TextView) findViewById(R.id.vaule2);
		tip_values[2] = (TextView) findViewById(R.id.vaule3);
		tip_values[3] = (TextView) findViewById(R.id.vaule4);

		for (int i = 0; i < bar.length; i++) {
			value[i] = 0;
			MyConnect.lastOkValue[i] = 0;
			bar[i].setOnSeekBarChangeListener(this);
		}

		/*
		 * bar1 = (SeekBar) findViewById(R.id.SeekBar1); bar2 = (SeekBar)
		 * findViewById(R.id.SeekBar2); bar3 = (SeekBar)
		 * findViewById(R.id.SeekBar3); bar4 = (SeekBar)
		 * findViewById(R.id.SeekBar4);
		 * 
		 * bar1.setOnSeekBarChangeListener(this);
		 * bar2.setOnSeekBarChangeListener(this);
		 * bar3.setOnSeekBarChangeListener(this);
		 * bar4.setOnSeekBarChangeListener(this);
		 */

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
			Intent intent = new Intent(MainActivityCtr.this,
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
			// freshData();

		}
			break;
		case R.id.btnQue: {
			// �����ѯ����¼�
			Intent intent = new Intent(MainActivityCtr.this,
					MainActivityQue.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnCtr: {
			// ������ƺ���¼�
			// Intent intent = new Intent(MainActivityCtr.this,
			// MainActivityCtr.class);
			// startActivity(intent);
		}
			break;
		case R.id.btnSet: {
			// ������ú���¼�
			Intent intent = new Intent(MainActivityCtr.this,
					MainActivitySet.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnHis: {
			// �����ʷ����¼�
			Intent intent = new Intent(MainActivityCtr.this,
					MainActivityHis.class);
			startActivity(intent);
			finish();
		}
			break;
		/*
		 * case R.id.togglebutton1: { // �������1����¼�
		 * 
		 * send(1, togglebutton1.isChecked());
		 * 
		 * if (togglebutton1.isChecked()) {
		 * 
		 * } // ����ť�ٴα����ʱ����Ӧ���¼� else {
		 * 
		 * }
		 * 
		 * 
		 * } break; case R.id.togglebutton2: { // �������2����¼�
		 * 
		 * send(2, togglebutton2.isChecked());
		 * 
		 * if (togglebutton2.isChecked()) {
		 * 
		 * } // ����ť�ٴα����ʱ����Ӧ���¼� else {
		 * 
		 * }
		 * 
		 * 
		 * } break;
		 * 
		 * case R.id.togglebutton3: { // �������3����¼�
		 * 
		 * send(3, togglebutton3.isChecked());
		 * 
		 * 
		 * if (togglebutton3.isChecked()) {
		 * 
		 * } // ����ť�ٴα����ʱ����Ӧ���¼� else {
		 * 
		 * }
		 * 
		 * 
		 * } break;
		 * 
		 * case R.id.togglebutton4: { // �������4����¼�
		 * 
		 * send(4, togglebutton4.isChecked());
		 * 
		 * 
		 * if (togglebutton4.isChecked()) {
		 * 
		 * } // ����ť�ٴα����ʱ����Ӧ���¼� else {
		 * 
		 * }
		 * 
		 * 
		 * } break;
		 */

		}
	}

	/*
	 * // ���Ϳ����źţ� void send(int btNo, boolean b) { // btNo�ǵڼ��������źţ�b�Ƿ��Ϳ������ǹر�
	 * 
	 * // ��ǰѡ�������Id �� spinner_sel
	 * 
	 * // δд��ͨ������ID spinner_sel �����������Ϳ����ź�
	 * 
	 * }
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

	void setButton(ToggleButton b) {
		if (b.isChecked()) {
			b.setChecked(false);
		}
		// ����ť�ٴα����ʱ����Ӧ���¼�
		else {
			b.setChecked(true);
		}
	}

	@Override
	public void onProgressChanged(SeekBar v, int arg1, boolean arg2) {
		//���϶���״̬�����仯ʱ����������arg1:��ǰseekbar��ֵ��0~100
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.SeekBar1: {
			setSeekBar(0, arg1, 1);

		}
			break;
		case R.id.SeekBar2: {
			setSeekBar(1, arg1, 1);

		}
			break;
		case R.id.SeekBar3: {
			setSeekBar(2, arg1, 1);
		}
			break;
		case R.id.SeekBar4: {
			setSeekBar(3, arg1, 1);
		}
			break;
		}
	}

	public void setSeekBar(int which, int progress, int sys) {
		//����seekbar��ֵ
		//����which:�������ĸ��豸�ĵ��ο��أ�progress���ÿ��ص�ֵ��sys:���sysΪ1��progress��ֵΪ0-100�����sysΪ0������progress��ֵΪ-2~2
		int temp;
		if (sys == 1) {
			temp = (progress - 50) / 25;
		} else {
			temp = progress;
		}

		value[which] = temp;
		tip_values[which].setText("��ǰ���Σ�" + value[which]);
		// int temp = Integer.valueOf(progress);
		switch (temp) {
		case MAX:
			bar[which].setProgress(100);
			break;
		case SEC_MAX:
			bar[which].setProgress(75);
			break;
		case MIDDLE:
			bar[which].setProgress(50);
			break;
		case SEC_MIN:
			bar[which].setProgress(25);
			break;
		case MIN:
			bar[which].setProgress(0);
			break;
		default:
			bar[which].setProgress(0);
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar v) {
		//������ֹͣ�϶�ʱ�������������������Ӧ�豸�Ŀ��Ƶ���
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.SeekBar1: {
			send(0);

		}
			break;
		case R.id.SeekBar2: {
			send(1);

		}
			break;
		case R.id.SeekBar3: {
			send(2);
		}
			break;
		case R.id.SeekBar4: {
			send(3);
		}
			break;
		}

	}

	void send(int i) {// ��������
		// �豸i(0~3),ֵΪvalue[i];
		/*
		 * Button refresh_greenhouse = (Button)
		 * findViewById(R.id.refresh_greenhouse);
		 * refresh_greenhouse.setText(i+" "+value[i]);
		 */
		eqpId = i;
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
			Toast toast = Toast.makeText(getApplicationContext(), "�����źŷ��ͳɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			String err_msg="���ӷ�������ʱ��";
			if(ret==-1)
				err_msg="����ʧ�ܣ�";
			Toast toast = Toast.makeText(getApplicationContext(),err_msg ,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			freshData();
		}

		/*
		 * 
		 * con.ctrSend(spinner_sel, i, value[i]);
		 */
	}

	void freshData() {//����������ʧ�ܡ�����������ʧ�ܵ��������ʱ���ã������ο�������Ϊ��һ�����óɹ���״̬

		for (int i = 0; i < 4; i++) {
			setSeekBar(i, MyConnect.lastOkValue[i], 0);
			tip_values[i].setText("��ǰ���Σ�" + MyConnect.lastOkValue[i]);
		}

		// ��ǰѡ�������Id �� spinner_sel
		/*
		 * boolean checked1, checked2, checked3, checked4; checked1 = checked2 =
		 * checked3 = checked4 = false;
		 * 
		 * // δд��ͨ������ID spinner_sel �ӷ���������checked1,checked2,checked3,checked4 ,
		 * // �ı�ؼ���״̬
		 * 
		 * // �ӷ���������value[0~3] // String val[]=new String[4];
		 * con.ctrRecv(spinner_sel, value);
		 * 
		 * for (int i = 0; i < 4; i++) { setSeekBar(i, value[i], 0);
		 * tip_values[i].setText("��ǰ���Σ�" + value[i]); }
		 * 
		 * Toast toast = Toast.makeText(getApplicationContext(), "ˢ�³ɹ���",
		 * Toast.LENGTH_SHORT); toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show();
		 * 
		 * 
		 * 
		 * togglebutton1.setChecked(checked1);
		 * togglebutton2.setChecked(checked2);
		 * togglebutton3.setChecked(checked3);
		 * togglebutton4.setChecked(checked4);
		 * 
		 * 
		 * // setButton(togglebutton1); // setButton(togglebutton2); //
		 * setButton(togglebutton3); // setButton(togglebutton4);
		 */
	}

}
