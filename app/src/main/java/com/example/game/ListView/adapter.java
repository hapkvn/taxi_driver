package com.example.game.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.game.R;

import java.util.List;

public class adapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<listUser> ListUserr;

    public adapter(Context context, int layout, List<listUser> listUserr) {
        this.context = context;
        this.layout = layout;
        ListUserr = listUserr;
    }

    @Override
    public int getCount() {
        return ListUserr.size();
    }

    @Override
    public Object getItem(int position) {
        return ListUserr.get(position); // Đã sửa lại cho đúng chuẩn thay vì return null
    }

    @Override
    public long getItemId(int position) {
        return position; // Đã sửa lại cho đúng chuẩn thay vì return 0
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tối ưu hóa: Chỉ tạo mới View nếu nó chưa tồn tại (giúp danh sách cuộn mượt hơn)
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        TextView userName = convertView.findViewById(R.id.txtUserLs);
        TextView point = convertView.findViewById(R.id.txtPointLs);
        ImageView imgSeaFood = convertView.findViewById(R.id.imageLS);

        listUser list_user = ListUserr.get(position);

        // HIỂN THỊ TÊN ĐẦY ĐỦ (fullName) LÊN MÀN HÌNH
        userName.setText(list_user.getFullName());
        point.setText(list_user.getPoint());
        imgSeaFood.setImageResource(R.drawable.list);

        return convertView;
    }
}