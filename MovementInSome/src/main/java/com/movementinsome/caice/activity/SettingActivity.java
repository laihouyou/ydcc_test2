package com.movementinsome.caice.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.movementinsome.kernel.initial.model.Number;

import java.util.ArrayList;
import java.util.List;


/**设置界面
 * Created by zzc on 2017/6/22.
 */

public class SettingActivity extends TitleBaseActivity{

    Spinner rangeSpinner;
    TextView den;

    private List<Long> ranges;
    private List<Number> numbers;

    private Long range;


    @Override
    protected void initTitle() {
        setTitle("设置");
        setTopLeftButton(R.drawable.black_while, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setTopRightButton("保存", new OnClickListener() {
            @Override
            public void onClick() {
                saveData();
            }
        });


    }

    @Override
    protected void initViews() {
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }


    private void initView() {
//        if (AppContext.getInstance().getCollectionScope()==0
//                ||AppContext.getInstance().getCollectionScope().equals("")){
//            AppContext.getInstance().setCollectionScope();
//        }

        rangeSpinner= (Spinner) findViewById(R.id.rangeSpinner);
        den= (TextView) findViewById(R.id.den);

        den.setText(AppContext.getInstance().getDem()+"米");

        if (ranges == null) {
            ranges = new ArrayList<>();
        }
        numbers = AppContext.getInstance().getSettingRanges();
        if (numbers != null && numbers.size() > 0) {
            for (int i = 0; i < numbers.size(); i++) {
                if (!numbers.get(i).getValue().equals("无限制")){
                    ranges.add(Long.parseLong(numbers.get(i).getValue()));
                }
            }
        }

        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(this, android.R.layout.simple_spinner_item, ranges);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        rangeSpinner.setAdapter(adapter);
        int position;
//        if (AppContext.getInstance().getCollectionScope().equals("0")){
//            position = ranges.lastIndexOf("无限制");
//        }else {
//        }
        position = ranges.lastIndexOf(AppContext.getInstance().getCollectionScope());
        rangeSpinner.setSelection(position, true);
        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                range = ranges.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void saveData() {
        if (range != null) {
//            if (range.equals("无限制")) {
//                if (AppContext.getInstance().setCollectionScope("0")){
//                    this.finish();
//                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//            }
            if (AppContext.getInstance().setCollectionScope(range)){
                this.finish();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
