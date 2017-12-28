package zskj.jkxt.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;

/**
 * Created by WYY on 2017/12/26.
 */

public class StatisticalDataAdapter extends PanelAdapter {

    private static final int TITLE_TYPE = 4;
    private static final int STATION_TYPE = 0;
    private static final int COL_TYPE = 1;
    private static final int DATA_TYPE = 2;

    private List<String> stationInfoList=new ArrayList<>();
    private List<String> colInfoList = new ArrayList<>();
    private List<List<String>> dataInfoList =new ArrayList<>();

    @Override
    public int getRowCount() {
        return stationInfoList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return colInfoList.size() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case COL_TYPE:
                setDetailView(column, (DetailViewHolder) holder, colInfoList);
                break;
            case STATION_TYPE:
                setDetailView(row, (DetailViewHolder) holder, stationInfoList);
                break;
            case DATA_TYPE:
                setDataView(row, column, (DataViewHolder) holder);
                break;
            case TITLE_TYPE:
                break;
            default:
                setDataView(row, column, (DataViewHolder) holder);
        }
    }

    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return STATION_TYPE;
        }
        if (row == 0) {
            return COL_TYPE;
        }
        return DATA_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case COL_TYPE:
                return new DetailViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_table_info, parent, false));
            case STATION_TYPE:
                return new DetailViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_table_info, parent, false));
            case DATA_TYPE:
                return new DataViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_table_info, parent, false));
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_title, parent, false));
            default:
                break;
        }
        return new DataViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_table_info, parent, false));
    }

    private void setDetailView(int pos, DetailViewHolder viewHolder, List<String> detailInfoList) {
        String detailInfo = detailInfoList.get(pos - 1);
        Log.e("detailInfo","--------->"+detailInfo);
        if (detailInfo != null && pos > 0) {
            viewHolder.detailTextView.setText(detailInfo);
        }
    }

    private void setDataView(final int row, final int column, DataViewHolder viewHolder) {
//        int[] colors = new int[]{0x30E0F3FA, 0x30FAFFFF};
        final String dataInfo = dataInfoList.get(column - 1).get(row - 1);
        Log.e("dataInfo","--------->"+dataInfo);
        if (dataInfo != null) {
            viewHolder.detailTextView.setText(dataInfo);
//            if(column % 2 == 1){
//                viewHolder.itemView.setBackgroundColor(colors[0]);
//            }else{
//                viewHolder.itemView.setBackgroundColor(colors[1]);
//            }
        }
    }

    private static class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView detailTextView;

        public DetailViewHolder(View itemView) {
            super(itemView);
            this.detailTextView = (TextView) itemView.findViewById(R.id.detail);
        }
    }

    private static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView detailTextView;
        public View view;

        public DataViewHolder(View view) {
            super(view);
            this.view = view;
            this.detailTextView = (TextView) view.findViewById(R.id.detail);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
        }
    }

    public void setStationInfoList(List<String> stationInfoList) {
        this.stationInfoList = stationInfoList;
    }
    public void setColInfoList(List<String> colInfoList) {
        this.colInfoList = colInfoList;
    }
    public void setDataInfoList(List<List<String>> dataInfoList) {
        this.dataInfoList = dataInfoList;
    }

}
