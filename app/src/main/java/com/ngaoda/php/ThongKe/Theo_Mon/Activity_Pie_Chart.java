package com.ngaoda.php.ThongKe.Theo_Mon;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ngaoda.php.QlDat.ClassMon;
import com.ngaoda.php.R;

import java.util.ArrayList;

public class Activity_Pie_Chart extends Activity {
    PieChart pieChart;
    ArrayList<ClassMon> classMons=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongke_theomon_activity__pie__chart);
        pieChart=findViewById(R.id.pie);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
           classMons=(ArrayList<ClassMon>) bundle.getSerializable("list");
        }
        ArrayList<PieEntry> data= new ArrayList<>();
        for(int i=0;i<classMons.size();i++){
            data.add(new PieEntry(classMons.get(i).sl,classMons.get(i).getTenmon()));
        }

        PieDataSet pieDataSet = new PieDataSet(data, "");
        pieDataSet.setSliceSpace(2);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(pieData);
        Description description =new Description();
        description.setText("");
        pieChart.setDescription(description);
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
    }
}
