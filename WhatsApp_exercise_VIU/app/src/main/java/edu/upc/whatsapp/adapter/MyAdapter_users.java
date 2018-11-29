/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.upc.whatsapp.R;
import entity.UserInfo;
import java.util.List;

/**
 *
 * @author upcnet
 */
public class MyAdapter_users extends BaseAdapter {

    Context context;
    public List<UserInfo> users;

    public MyAdapter_users(Context context, List<UserInfo> users) {
      this.context = context;
      this.users = users;
    }

    public int getCount() {
      return users.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_twotextviews, parent, false);
      }
          UserInfo render_user;
          render_user = users.get(position);
          ((TextView) convertView.findViewById(R.id.row_twotextviews_name)).setText(render_user.getName());
          ((TextView) convertView.findViewById(R.id.row_twotextviews_surname)).setText(render_user.getSurname());


      return convertView;
    }

    public Object getItem(int arg0) {
      return users.get(arg0);
    }

    public long getItemId(int arg0) {
      return users.get(arg0).getId();
    }
  }
