package com.plantapp1_0_0;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.refen.Shed;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

//import com.example.hellocharttest.R;

import android.app.DatePickerDialog;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityHis extends Activity implements View.OnClickListener {
//	MyConnect con;
	private long exitTime = 0;//���ڼ����û����������η��ؼ���ʱ�����������Ƿ��˳�����
	private int mYear1 = 2016, mMonth1 = 12, mDay1 = 1;// ��ʼ����
	private int mYear2 = 2016, mMonth2 = 12, mDay2 = 10;// ��������
	private int countDay = 0;// �������ڵļ��ʱ��
	DatePickerDialog datePickerDialog1 = null;
	DatePickerDialog datePickerDialog2 = null;
	private Button date1;// ��ʾ��ʼ���ڵĿؼ�
	private Button date2;// ��ʾ�������ڵĿؼ�
	// final private int DATE_DIALOG = 1;
	// private int dataSign = 1;
	private Spinner spinner_greenhouses2;// ѡ�����ҵ������б�
	private Spinner spinner_type;// ѡ���ѯ���͵������б�
	private int spinner_sel1 = 0;// ��ǰѡ��������������б�����
	private int spinner_sel2 = 0;// ��ǰѡ��Ĳ�ѯ�����������б�����
	private Button line_query;// ��ѯ��ť

	private LineChartView lineChart;
	String[] date = null;// X��ı�ע
	float[] score = null;// ͼ�������
	// float[] score2 = null;// ͼ�������2���������ݣ�

	/*
	 * String[] date = { "5-23", "5-22", "6-22", "5-23", "5-22", "2-22", "5-22",
	 * "4-22", "9-22", "10-22", "11-22", "12-22", "1-22", "6-22", "5-23",
	 * "5-22", "2-22", "5-22", "4-22", "9-22", "10-22", "11-22", "12-22",
	 * "4-22", "9-22", "10-22" };// X��ı�ע int[] score = { 74, 22, 18, 79, 20,
	 * 74, 20, 74, 42, 90, 74, 42, 90, 50, 42, 90, 33, 10, 74, 22, 18, 79, 20,
	 * 74, 22, 18, 79, 20, 0 };// ͼ�������
	 */

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
			// private int mYear1 , mMonth1, mDay1 ;// ��ʼ����
			// private int mYear2 , mMonth2 , mDay2;// ��������
			// ����ID��shedId[spinner_sel]
			// spinner_type �¶ȡ�ʪ�ȡ����յ����͵����
			
			String ip =url_que + "";//�˴���
			String data = Myconnect(ip);
			ret = his(data);
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

	public int his(String data) {
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
			
			List<Long> date1=new ArrayList<Long>();
			 List<Float> score1=new ArrayList<Float>();
			 JSONArray arr=(JSONArray) map.get("sheds");//�˴���
			 int n=0;
			 for(Object object:arr){
				 JSONObject shedjson=(JSONObject) object;
				 Shed shed=JSON.toJavaObject(shedjson, Shed.class);//�˴���
			/*	 date1.add(shed.getCode());//�˴���
				 score1.add(shed.getId());//�˴���
*/				 n++;				 
			 }	
			
			
			date = new String[n];			
			score = new float[n];
			
			for (int i = 0; i < date.length; i++) {				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date1.get(i));
				cal.set(mYear1, mMonth1 - 1, mDay1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				date[i] = sdf.format(cal.getTime());
				score[i]=score1.get(i);				
			}
			return 1;
		} else
			return -1;
	}

	private List<PointValue> mPointValues = null;
	// private List<PointValue> mPointValues2 = null;
	private List<AxisValue> mAxisXValues = null;

	private DatePickerDialog.OnDateSetListener mdateListener1 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear1 = year;
			mMonth1 = monthOfYear + 1;
			mDay1 = dayOfMonth;
			display(1);
		}
	};
	private DatePickerDialog.OnDateSetListener mdateListener2 = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear2 = year;
			mMonth2 = monthOfYear + 1;
			mDay2 = dayOfMonth;
			display(2);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_his);
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

		final Calendar ca = Calendar.getInstance();
		mYear2 = ca.get(Calendar.YEAR);
		mMonth2 = ca.get(Calendar.MONTH) + 1;
		mDay2 = ca.get(Calendar.DAY_OF_MONTH);

		ca.add(Calendar.DATE, -7);
		mYear1 = ca.get(Calendar.YEAR);
		mMonth1 = ca.get(Calendar.MONTH) + 1;
		mDay1 = ca.get(Calendar.DAY_OF_MONTH);

		date1 = (Button) findViewById(R.id.dateChoose1);
		date2 = (Button) findViewById(R.id.dateChoose2);

		date1.setOnClickListener(this);
		date2.setOnClickListener(this);

		/*
		 * date1.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // dataSign = 1; 555
		 * 
		 * } });
		 * 
		 * date2.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // dataSign = 2;
		 * showDialog(DATE_DIALOG + 1); // onCreateDialog(DATE_DIALOG+1); } });
		 */

		spinner_greenhouses2 = (Spinner) findViewById(R.id.spinner_greenhouse2);
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
		spinner_greenhouses2.setAdapter(adapter);
		spinner_greenhouses2.setPrompt("��ѡ�����ң�");
		spinner_greenhouses2.setSelection(0, true);

		spinner_greenhouses2
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						spinner_sel1 = arg2;
						// line_query.setText(spinner_sel1 + "" + spinner_sel2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		spinner_type = (Spinner) findViewById(R.id.spinner_type);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.typess, R.layout.my_simple_spinner_item);
		adapter2.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
		spinner_type.setAdapter(adapter2);
		spinner_type.setPrompt("��ѡ���ѯ���ͣ�");
		spinner_type.setSelection(0, true);
		spinner_type
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						spinner_sel2 = arg2;
						// line_query.setText(spinner_sel1 + "" + spinner_sel2);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		line_query = (Button) findViewById(R.id.line_query);
		line_query.setOnClickListener(this);

		// ����ͼ��
		lineChart = (LineChartView) findViewById(R.id.line_chart);

		freshView();

	}

	/*
	 * @Override protected Dialog onCreateDialog(int id) { switch (id) { case
	 * DATE_DIALOG: // removeDialog(DATE_DIALOG); return new
	 * DatePickerDialog(this, mdateListener1, mYear1, mMonth1 - 1, mDay1); case
	 * DATE_DIALOG + 1: // removeDialog(DATE_DIALOG+1); return new
	 * DatePickerDialog(this, mdateListener2, mYear2, mMonth2 - 1, mDay2); }
	 * return null; }
	 */

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
			Intent intent = new Intent(MainActivityHis.this,
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
		case R.id.dateChoose1: {
			// �������1����¼�
			if (datePickerDialog1 != null) {

				// ���״������������һ���޸ĵ����ڸ���datePickerDialog
				datePickerDialog1.updateDate(mYear1, mMonth1 - 1, mDay1);
			}
			// showDialog(DATE_DIALOG);
			else {

				// �״�����ʱ����context��OnDateSetListener����͵�ǰ�õ�ϵͳ���ڳ�ʼ��datePickerDialog
				// datePickerDialog1=new DatePickerDialog();
				datePickerDialog1 = new DatePickerDialog(this, mdateListener1,
						mYear1, mMonth1 - 1, mDay1);

			}
			datePickerDialog1.show();

		}
			break;
		case R.id.dateChoose2: {
			// �������2����¼�

			if (datePickerDialog2 != null) {

				// ���״������������һ���޸ĵ����ڸ���datePickerDialog
				datePickerDialog2.updateDate(mYear2, mMonth2 - 1, mDay2);
			}
			// showDialog(DATE_DIALOG);
			else {

				// �״�����ʱ����context��OnDateSetListener����͵�ǰ�õ�ϵͳ���ڳ�ʼ��datePickerDialog
				// datePickerDialog1=new DatePickerDialog();
				datePickerDialog2 = new DatePickerDialog(this, mdateListener2,
						mYear2, mMonth2 - 1, mDay2);

			}
			datePickerDialog2.show();

		}
			break;
		case R.id.btnQue: {
			// �����ѯ����¼�
			Intent intent = new Intent(MainActivityHis.this,
					MainActivityQue.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnCtr: {
			// ������ƺ���¼�
			Intent intent = new Intent(MainActivityHis.this,
					MainActivityCtr.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnSet: {
			// ������ú���¼�
			Intent intent = new Intent(MainActivityHis.this,
					MainActivitySet.class);
			startActivity(intent);
			finish();
		}
			break;
		case R.id.btnHis: {
			// �����ʷ����¼�
			// Intent intent = new Intent(MainActivityHis.this,
			// MainActivityHis.class);
			// startActivity(intent);
		}
			break;
		case R.id.line_query: {
			// �����ѯ����¼�
			checkDate();
			date1.setText(new StringBuffer().append(mYear1).append("-")
					.append(mMonth1).append("-").append(mDay1).append(" "));
			date2.setText(new StringBuffer().append(mYear2).append("-")
					.append(mMonth2).append("-").append(mDay2).append(" "));
			// line_query.setText(spinner_sel1+""+spinner_sel2);

			freshView();
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

	/**
	 * �������� ����StringBuffer׷��
	 */
	public void display(int dataSign) {
		if (dataSign == 1)
			date1.setText(new StringBuffer().append(mYear1).append("-")
					.append(mMonth1).append("-").append(mDay1).append(" "));
		else
			date2.setText(new StringBuffer().append(mYear2).append("-")
					.append(mMonth2).append("-").append(mDay2).append(" "));
	}

	void checkDate() {// ��������
		if (mYear1 < 2010)
			mYear1 = 2010;
		if (mYear2 < 2010)
			mYear2 = 2010;
		Calendar ca = Calendar.getInstance();
		int tYear1 = ca.get(Calendar.YEAR);
		int tMonth1 = ca.get(Calendar.MONTH) + 1;
		int tDay1 = ca.get(Calendar.DAY_OF_MONTH);

		if (!IfDate1Smaller(mYear1, mMonth1, mDay1, tYear1, tMonth1, tDay1)) {
			mYear1 = tYear1;
			mMonth1 = tMonth1;
			mDay1 = tDay1;
		}
		if (!IfDate1Smaller(mYear2, mMonth2, mDay2, tYear1, tMonth1, tDay1)) {
			mYear2 = tYear1;
			mMonth2 = tMonth1;
			mDay2 = tDay1;
		}
		if (!IfDate1Smaller(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2)) {
			int tem = mYear1;
			mYear1 = mYear2;
			mYear2 = tem;
			tem = mMonth1;
			mMonth1 = mMonth2;
			mMonth2 = tem;
			tem = mDay1;
			mDay1 = mDay2;
			mDay2 = tem;
		}

		if (mYear1 == mYear2 && mMonth1 == mMonth2 && mDay1 == mDay2) {
			ca.set(mYear2, mMonth2 - 1, mDay2);
			ca.add(Calendar.DATE, -7);
			mYear1 = ca.get(Calendar.YEAR);
			mMonth1 = ca.get(Calendar.MONTH) + 1;
			mDay1 = ca.get(Calendar.DAY_OF_MONTH);
		}

	}

	boolean IfDate1Smaller(int y1, int m1, int d1, int y2, int m2, int d2) {// ����1�Ƿ�С������2
		if (y1 < y2)
			return true;
		else if (y1 > y2)
			return false;
		else {
			if (m1 < m2)
				return true;
			else if (m1 > m2)
				return false;
			else {
				if (d1 < d2)
					return true;
				else
					// if(d1>d2)and if(d1==d2)
					return false;
			}
		}

	}

	public static int daysBetween(int y1, int m1, int d1, int y2, int m2, int d2) {
		m1--;
		m2--;
		Calendar cal = Calendar.getInstance();
		cal.set(y1, m1, d1);
		long time1 = cal.getTimeInMillis();
		// Calendar cal2 = Calendar.getInstance();
		cal.set(y2, m2, d2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	

	/**
	 * ��ʼ��LineChart��һЩ����
	 */
	private void initLineChart() {
		Line line = new Line(mPointValues)
				.setColor(Color.parseColor("#33CD41")); // ���ߵ���ɫ
		List<Line> lines = new ArrayList<Line>();
		line.setShape(ValueShape.CIRCLE);// ����ͼ��ÿ�����ݵ����״ ������Բ�� ��������
											// ��ValueShape.SQUARE
											// ValueShape.CIRCLE
											// ValueShape.SQUARE��
		line.setCubic(false);// �����Ƿ�ƽ��
		// line.setStrokeWidth(3);//�����Ĵ�ϸ��Ĭ����3
		line.setFilled(false);// �Ƿ�������ߵ����
		line.setHasLabels(true);// ���ߵ����������Ƿ���ϱ�ע
		// line.setHasLabelsOnlyForSelected(true);//�������������ʾ���ݣ����������line.setHasLabels(true);����Ч��
		line.setHasLines(true);// �Ƿ���ֱ����ʾ�����Ϊfalse ��û������ֻ�е���ʾ
		line.setHasPoints(true);// �Ƿ���ʾԲ�� ���Ϊfalse ��û��ԭ��ֻ�е���ʾ
		lines.add(line);

		/*
		 * line = new Line(mPointValues2).setColor(Color.parseColor("#FFCD41"));
		 * // ���ߵ���ɫ line.setShape(ValueShape.SQUARE);// ����ͼ��ÿ�����ݵ����״ ������Բ�� ��������
		 * // ��ValueShape.SQUARE // ValueShape.CIRCLE // ValueShape.SQUARE��
		 * line.setCubic(false);// �����Ƿ�ƽ�� // line.setStrokeWidth(3);//�����Ĵ�ϸ��Ĭ����3
		 * line.setFilled(false);// �Ƿ�������ߵ���� line.setHasLabels(true);//
		 * ���ߵ����������Ƿ���ϱ�ע //
		 * line.setHasLabelsOnlyForSelected(true);//�������������ʾ���ݣ����������line
		 * .setHasLabels(true);����Ч�� line.setHasLines(true);// �Ƿ���ֱ����ʾ�����Ϊfalse
		 * ��û������ֻ�е���ʾ line.setHasPoints(true);// �Ƿ���ʾԲ�� ���Ϊfalse ��û��ԭ��ֻ�е���ʾ
		 * 
		 * lines.add(line);
		 */
		LineChartData data = new LineChartData();
		data.setLines(lines);

		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(false); // X������������������б����ʾ����ֱ�ģ�true��б����ʾ
		// axisX.setTextColor(Color.WHITE); //����������ɫ
		axisX.setTextColor(Color.parseColor("#D6D6D9"));// ��ɫ

		String name = spinner_greenhouses2.getSelectedItem().toString()
				+ spinner_type.getSelectedItem().toString() + "����ͼ";//

		axisX.setName(name); // �������
		axisX.setTextSize(18);// ���������С
		axisX.setMaxLabelChars(7); // ��༸��X�����꣬��˼�������������X�������ݵĸ���7<=x<=mAxisValues.length
		axisX.setValues(mAxisXValues); // ���X�����������
		data.setAxisXBottom(axisX); // x ���ڵײ�
		// data.setAxisXTop(axisX); //x ���ڶ���
		axisX.setHasLines(true); // x ��ָ���

		Axis axisY = new Axis(); // Y��
		String name2 = spinner_type.getSelectedItem().toString();
		if (name2.equals("�����¶�") || name2.equals("�����¶�")) {
			name2 = name2 + "����λ����";
		} else if (spinner_sel2 == 3) {
			// name2.equals("CO2Ũ��")
			name2 = "CO2Ũ�ȣ���λ����";
		} else if (name2.equals("ʪ��")) {
			name2 = "ʪ�ȣ���λ����";
		} else {// ����
			name2 = "���գ���λ��lux";
			// name2 = spinner_sel1 + "";
		}
		axisY.setName(name2);// y���ע
		axisY.setTextSize(14);// ���������С
		data.setAxisYLeft(axisY); // Y�����������
		// data.setAxisYRight(axisY); //y���������ұ�
		// ������Ϊ���ԣ�֧�����š������Լ�ƽ��
		lineChart.setInteractive(true);
		lineChart.setZoomType(ZoomType.HORIZONTAL); // �������ͣ�ˮƽ
		lineChart.setMaxZoom((float) 300);// ���ű���
		lineChart.setLineChartData(data);
		lineChart.setVisibility(View.VISIBLE);

		Viewport v = new Viewport(lineChart.getMaximumViewport());
		v.left = 0;
		v.right = 7;
		lineChart.setCurrentViewport(v);
	}

	
	/**
	 * X �����ʾ
	 */
	private void getAxisXLables() {
		countDay = daysBetween(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2) + 1;
		date = new String[countDay];

		Calendar cal = Calendar.getInstance();
		cal.set(mYear1, mMonth1 - 1, mDay1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// String dateStr = sdf.format(cal.getTime());
		mAxisXValues = new ArrayList<AxisValue>();

		for (int i = 0; i < date.length; i++) {
			date[i] = sdf.format(cal.getTime());
			mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
			cal.add(Calendar.DATE, 1);// ��
		}
	}

	/**
	 * ͼ���ÿ�������ʾ
	 */
	private void getAxisPoints() {
		countDay = daysBetween(mYear1, mMonth1, mDay1, mYear2, mMonth2, mDay2) + 1;
		score = new float[countDay];
		// score2 = new float[countDay];
		getScore();
		mPointValues = new ArrayList<PointValue>();
		// mPointValues2 = new ArrayList<PointValue>();
		for (int i = 0; i < score.length; i++) {
			mPointValues.add(new PointValue(i, score[i]));
			// mPointValues2.add(new PointValue(i, score2[i]));
		}
	}
	
	void getScore() {

		for (int i = 0; i < score.length; i++) {

			// δд���ӷ���������score����

			// score���������ݣ�score2��������

			// ��ʼ���� int mYear1, mMonth1, mDay1 ;
			// �������� int mYear2, mMonth2 , mDay2 ;
			// ��ǰѡ�������Id int spinner_sel1;
			// ��ǰѡ��Ĳ�ѯ����Id int spinner_sel2;

			int max = 1;
			int min = 0;
			Random random = new Random();

			score[i] = (float) ((random.nextInt(max) % (max - min + 1) + min) / 10.0);
			// score2[i] = (float) ((random.nextInt(max) % (max - min + 1) +
			// min) / 10.0);
		}

	}

	void freshView() {
		
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
			Toast toast = Toast.makeText(getApplicationContext(), "��ѯ�ɹ���",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			
			//��ʼ��x��:
			
			mAxisXValues = new ArrayList<AxisValue>();

			for (int i = 0; i < date.length; i++) {
				//date[i] = sdf.format(cal.getTime());
				mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
			}
			
			//��ʼ��y�����ݣ�
			mPointValues = new ArrayList<PointValue>();
			
			for (int i = 0; i < score.length; i++) {
				mPointValues.add(new PointValue(i, score[i]));
				// mPointValues2.add(new PointValue(i, score2[i]));
			}
			
			
		} else {
			String err_msg = "���ӷ�������ʱ��";
			if (ret == -1)
				err_msg = "��ѯʧ��,û����Ӧ�����ݣ�";
			Toast toast = Toast.makeText(getApplicationContext(), err_msg,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			
			getAxisXLables();// ��ȡx��ı�ע
			getAxisPoints();// ��ȡ�����
			initLineChart();// ��ʼ��

			
		}

		

	}

	/*
	 * @Override public void onDateSet(DatePicker view, int year, int
	 * monthOfYear, int dayOfMonth) { // TODO Auto-generated method stub if
	 * ((datePickerDialog1 != null) && (view.getId() ==
	 * datePickerDialog1.getDatePicker().getId())) {
	 * 
	 * // ����TextView,�����޸ĺ��ֵ���棻 mYear1 = year; mMonth1 = monthOfYear + 1; mDay1
	 * = dayOfMonth; display(1); } if ((datePickerDialog2 != null) &&
	 * (view.getId() == datePickerDialog2.getDatePicker().getId())) {
	 * 
	 * // ����TextView,�����޸ĺ��ֵ���棻 // year=0; mYear2 = year; mMonth2 = monthOfYear
	 * + 1; mDay2 = dayOfMonth; display(2); } if ((datePickerDialog1 != null) &&
	 * (datePickerDialog2 != null)) { if
	 * (datePickerDialog1.getDatePicker().getId() == datePickerDialog2
	 * .getDatePicker().getId()) { mYear2 = 1; display(2); } } }
	 */

}
