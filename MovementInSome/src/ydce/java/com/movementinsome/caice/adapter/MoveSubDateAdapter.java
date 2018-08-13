package com.movementinsome.caice.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.R;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpURL;
import com.movementinsome.caice.vo.HistoryCommitVO;
import com.movementinsome.caice.vo.IsUpdateVo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * Created by zzc on 2017/3/17.
 */

public class MoveSubDateAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryCommitVO> mHistoryCommitVOs;
    private Handler mHandler;
    private Dao<HistoryCommitVO,Long> mHistoryCommitVOLongDao;

    public MoveSubDateAdapter(Context context, List<HistoryCommitVO> historyCommitVOs,
                              Handler handler, Dao<HistoryCommitVO,Long> historyCommitVOLongDao){
        this.mContext=context;
        this.mHistoryCommitVOs=historyCommitVOs;
        this.mHandler=handler;
        this.mHistoryCommitVOLongDao=historyCommitVOLongDao;
    }

    @Override
    public int getCount() {
        return mHistoryCommitVOs.size();
    }

    @Override
    public Object getItem(int i) {
        return mHistoryCommitVOs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.move_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.tvFacnum= (TextView) view.findViewById(R.id.tvFacnum);
            viewHolder.tvTime= (TextView) view.findViewById(R.id.tvTime);
            viewHolder.btnChakan= (Button) view.findViewById(R.id.btnChakan);
            viewHolder.btnTijiao= (Button) view.findViewById(R.id.btnTijiao);

            viewHolder.tvFacnum.setText(mHistoryCommitVOs.get(i).getTableNum());
            viewHolder.tvTime.setText(mHistoryCommitVOs.get(i).getUploadTime());
            viewHolder.btnChakan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tv = new TextView(mContext);
                    tv.setText("表号:" + mHistoryCommitVOs.get(i).getTableNum() + "\n"
                            + "口径:" + mHistoryCommitVOs.get(i).getCaliber() + "\n"
                            + "地址:" + mHistoryCommitVOs.get(i).getAddr() + "\n"
                            + "经纬度:" + "(" + mHistoryCommitVOs.get(i).getLongitude()
                            + "," + mHistoryCommitVOs.get(i).getLatitude() + ")" + "\n"
                            + "时间:" + mHistoryCommitVOs.get(i).getUploadTime());
                    new AlertDialog.Builder(mContext)
                            .setTitle("详细信息")
                            .setView(tv)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            });

            viewHolder.btnTijiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map parameterMap = new HashMap();
                    parameterMap.put("title", "gddst");
                    parameterMap.put("address", mHistoryCommitVOs.get(i).getAddr());
                    parameterMap.put("latitude", mHistoryCommitVOs.get(i).getLatitude());
                    parameterMap.put("longitude", mHistoryCommitVOs.get(i).getLongitude());
                    parameterMap.put(OkHttpParam.COORD_TYPE, OkHttpParam.COORD_TYPE_VALUE);
                    parameterMap.put(OkHttpParam.GEOTABLE_ID, OkHttpParam.GEOIAVLE_VALUE);
                    parameterMap.put(OkHttpParam.AK, OkHttpParam.AK_VALUE);
                    parameterMap.put("fac_num", mHistoryCommitVOs.get(i).getTableNum());//设施编号

                    OkHttpUtils.post()
                            .url(OkHttpURL.urlGeodata)
                            .params(parameterMap)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext,
                                            "上传点位数据失败,请检查网络设置", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.i(TAG, response);
                                    IsUpdateVo isUpdateVo = JSON.parseObject(response, IsUpdateVo.class);
                                    if (isUpdateVo.getCode() == 0) {
                                        Toast.makeText(mContext, "提交数据成功", Toast.LENGTH_SHORT).show();
//                                        mContext.getLatlogData();
                                        try {
                                            int s = mHistoryCommitVOLongDao.delete(mHistoryCommitVOs.get(i));
                                            if (s == 1) {
                                                Toast.makeText(mContext,
                                                        "删除成功", Toast.LENGTH_LONG).show();
//                                                historyCommitVOs.remove(position);
                                                Message msg = new Message();
                                                msg.what = 4;
                                                mHandler.sendMessage(msg);
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "提交数据失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder {
        TextView tvFacnum;
        TextView tvTime;
        Button btnChakan;
        Button btnTijiao;
    }


    public List<HistoryCommitVO> getmHistoryCommitVOs() {
        return mHistoryCommitVOs;
    }

    public void setmHistoryCommitVOs(List<HistoryCommitVO> mHistoryCommitVOs) {
        this.mHistoryCommitVOs = mHistoryCommitVOs;
    }
}
