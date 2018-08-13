package com.movementinsome.map.nearby;

import java.util.List;

import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TableOnClickListener implements OnClickListener {

	private String tableType;
	private Context context;
	private List<Module> lstModule;
	public TableOnClickListener(String tableType,Context context,List<Module> lstModule){
		this.tableType=tableType;
		this.context=context;
		this.lstModule=lstModule;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Module module = isXMLForm(tableType);
		if (module!=null){
			//
			Intent newFormInfo = new Intent(context,RunForm.class);
    		newFormInfo.putExtra("template", module.getTemplate());		
    		context.startActivity(newFormInfo);
		}else{
			try {
				context.startActivity(new Intent(tableType));
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(context, "该功能未授权使用，请联系软件提供商！", 0).show();
			}
		}
	}
	public Module isXMLForm(String id){
		for(Module module:lstModule){
			if (id.equals(module.getId())){
				if ("XML".equalsIgnoreCase(module.getFormtype())){
					return module;
				}else{
					return null;
				}
			}
		}
		return null;
	}
}
