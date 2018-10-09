package edu.upc.repairs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import entity.WorkOrder;
import java.util.List;
import edu.upc.repairs.R;

/**
 *
 * @author juanluis
 */
public class MyAdapter_WorkOrders extends BaseAdapter {

    Context context;
    public List<WorkOrder> workOrders;

    public MyAdapter_WorkOrders(Context context, List<WorkOrder> workOrders) {
      this.context = context;
      this.workOrders = workOrders;
    }

    public int getCount() {
      return workOrders.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_twotextviews, parent, false);
      }

      WorkOrder workOrder = workOrders.get(position);
      TextView tv1 = (TextView) convertView.findViewById(R.id.row_twotextviews_tv1);
      TextView tv2 = (TextView) convertView.findViewById(R.id.row_twotextviews_tv2);
      tv1.setText(workOrder.getClient().getName());
      tv2.setText(workOrder.getTitle());
      return convertView;
    }

    public Object getItem(int arg0) {
      return workOrders.get(arg0);
    }

    public long getItemId(int arg0) {
      return workOrders.get(arg0).getId();
    }
  }
