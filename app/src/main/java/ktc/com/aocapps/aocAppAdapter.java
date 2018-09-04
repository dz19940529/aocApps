package ktc.com.aocapps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.transform.Source;

/**
 * Created by daic on 2018/8/18.
 */

class aocAppAdapter extends BaseAdapter{

    private final PackageManager mPManager;
    private  Context mContext;
    private ArrayList<ResolveInfo> mDate;

    public aocAppAdapter(Context context, ArrayList<ResolveInfo> date) {
         mContext = context;
        mDate = date;
        mPManager = mContext.getApplicationContext().getPackageManager();

    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int i) {
        return mDate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CustomViewHolder holder;
        if (convertView != null) {
            holder = (CustomViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_base_app, null);
            holder = new CustomViewHolder(convertView);
            convertView.setTag(holder);
        }
        initView(holder, i);
        return convertView;
    }

    private void initView (CustomViewHolder holder, int pos) {

        final ResolveInfo info = mDate.get(pos);
        if(info.loadLabel(mPManager).toString().equals(mContext.getResources().getString(R.string.philipsstore))){
            holder.title.setText(mContext.getResources().getString(R.string.AOCstore));
        }else {
            holder.title.setText(info.loadLabel(mPManager).toString());
        }
        holder.icon.setImageDrawable(info.loadIcon(mPManager));
    }

    class CustomViewHolder {

        private View itemView;
        private TextView title;
        private ImageView icon;

        CustomViewHolder (View itemView) {
            this.itemView = itemView;
            initView(itemView);
        }

        void initView (View view) {
            title = (TextView) view.findViewById(R.id.title);
            icon = (ImageView) view.findViewById(R.id.icon);
        }

    }
}
