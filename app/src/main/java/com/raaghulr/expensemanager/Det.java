package com.raaghulr.expensemanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class Det extends Activity {
	Button btnAdd;
	ExpenseDataBaseAdapter expenseDataBaseAdapter;
	LoginDataBaseAdapter loginDataBaseAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_det);
		
		expenseDataBaseAdapter=new ExpenseDataBaseAdapter(this);
	    expenseDataBaseAdapter=expenseDataBaseAdapter.open();
		
		
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
	    loginDataBaseAdapter=loginDataBaseAdapter.open();
	    btnAdd=(Button)findViewById(R.id.addExp);
		
	    
	    
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String x = preferences.getString("username","");
		Integer expend = loginDataBaseAdapter.getexpendEntry(x);
		((TextView)findViewById(R.id.expenseview)).setText(Integer.toString(expend));
		TextView btndisp = (TextView)findViewById(R.id.expenseview);
		btndisp.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v2) { 
				
				Intent inte=new Intent(getApplicationContext(),Displaydetlist.class);
				startActivity(inte);
			}});
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String currentDateandTime = sdf.format(new Date());
		
		((TextView)findViewById(R.id.date)).setText(currentDateandTime);
		
		
	}
	public void addexp(View V)
	   {
			final Dialog dialog = new Dialog(Det.this);
			dialog.setContentView(R.layout.add);
		    dialog.setTitle("Add Expense");
		    
		    
		   
		    final  EditText editTextdate=(EditText)dialog.findViewById(R.id.dateentry);
		    final  EditText editTextexpense=(EditText)dialog.findViewById(R.id.expenseentry);
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String currentDateandTime = sdf.format(new Date());
		    editTextdate.setText(currentDateandTime);
		    
			Button btnAdd=(Button)dialog.findViewById(R.id.addexp);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			final String x = preferences.getString("username","");
			final Integer expend = loginDataBaseAdapter.getexpendEntry(x);
			
				
			// Set On ClickListener
			btnAdd.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// get The User name and Password
					String Date=editTextdate.getText().toString();
					String E  = editTextexpense.getText().toString();
					
					if(Date.equals("")||E.equals(""))
					{
							Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
							return;
					}
					Integer Expense = Integer.parseInt(E);
					Integer Expend= expend - Expense;
					String uName = x;
					String Password=loginDataBaseAdapter.getSinlgeEntry(uName);
									
					Integer dailyexp= expenseDataBaseAdapter.getexpensefromdate(uName,Date);
					
					if(Expend<0)
					{
						Toast.makeText(getApplicationContext(), "Insufficient Funds", Toast.LENGTH_LONG).show();
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "Please Increment your Budget", Toast.LENGTH_LONG).show();
					}
					
					else
					{
						if(dailyexp==0){
						expenseDataBaseAdapter.insertEntry(uName, Date, Expense);
						loginDataBaseAdapter.updateEntry(uName,Password,Expend);			
						
						
						
							Toast.makeText(Det.this, "Expense Added", Toast.LENGTH_LONG).show();
							dialog.dismiss();
							
							Intent intent=new Intent(getApplicationContext(),Det.class);
							startActivity(intent);
							
							
						}
						else
						{
							Integer upexp=dailyexp+Expense;
							expenseDataBaseAdapter.updateEntry(uName, Date, upexp);
							loginDataBaseAdapter.updateEntry(uName,Password,Expend);
							
							Toast.makeText(Det.this, "Expense Added", Toast.LENGTH_LONG).show();
							dialog.dismiss();
							
							Intent intent=new Intent(getApplicationContext(),Det.class);
							startActivity(intent);
						}
					}
					
					
				}
			});
			
			dialog.show();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		expenseDataBaseAdapter.close();
	}
	
	int backButtonCount=0;
	@Override
	public void onBackPressed()
	{
	   
		if(backButtonCount >= 1)
	    {
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_HOME);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	    }
	    else
	    {
	        Toast.makeText(this, "Press the back button once again to exit", Toast.LENGTH_SHORT).show();
	        backButtonCount++;
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu2, menu);
		return true;
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	            case R.id.logout:
	            	Intent i5 = new Intent(getApplicationContext(), HomeActivity.class);
				      startActivity(i5);
				      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("username","");
						editor.commit();
						Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_LONG).show();
	                return true;
	            case R.id.calc:
	            
	            	 Intent ic= new Intent(); 
				       ic.setClassName("com.android.calculator2","com.android.calculator2.Calculator");
			
				       try {
				          startActivity(ic);
				       } catch (ActivityNotFoundException noSuchActivity) {
				    	   Intent mIntent = getPackageManager().getLaunchIntentForPackage(
				    			    "com.sec.android.app.popupcalculator");

				    	   try{
				    			startActivity(mIntent);
				    	   }
				    	   catch (ActivityNotFoundException noActivity){
				    	   Toast.makeText(getApplicationContext(), "Calculator Not Installed", Toast.LENGTH_LONG).show();
				       }}
				       break;
	            case R.id.aboutus:
	            	Intent ia =new Intent(getApplicationContext(), About.class);
	            	startActivity(ia);
	            	break;
	                
	            case R.id.abudget:
	            Intent i6 = new Intent(getApplicationContext(), Addbu.class);
	            	startActivity(i6);
	            	break;
	            	
	            case R.id.tog:
	            	 Intent intd = new Intent(getApplicationContext(), Ret.class);
				      startActivity(intd);
				      break;
	            
	            
	           default:
	                return super.onOptionsItemSelected(item);
	        }
			return false;
	 }}