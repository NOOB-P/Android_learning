package com.example.android_leraning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_leraning.databinding.ActivityNewsBinding;
import com.example.android_leraning.databinding.NewItem1Binding;
import com.example.android_leraning.databinding.NewItem2Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class News extends AppCompatActivity {
    class NewBean{
        public String title; //标题
        public String time; //时间
        public int comment; //评论数
        public String name; //用户名
        public int type; //类型
        public List<Integer> imgList; //图片列表
        public NewBean(String title, String time, int comment, String name, int type,  List<Integer> imgList){
            this.comment = comment;
            this.imgList = imgList;
            this.title = title;
            this.time  = time;
            this.name = name;
            this.type = type;
        }
    }
    ActivityNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<NewBean> newsList = new ArrayList<>();

        // 单图新闻 (type=1)
        newsList.add(new NewBean("雷军宣布小米汽车首日订单破10万", "刚刚", 1256,
                "科技前沿", 1, Arrays.asList()));

        newsList.add(new NewBean("NASA发现潜在宜居行星", "2小时前", 892,
                "太空探索", 1, Arrays.asList(R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("2024全球开发者大会日程公布", "昨天", 431,
                "IT快讯", 1, Arrays.asList(R.drawable.ic_launcher_foreground)));

        // 多图新闻 (type=2)
        newsList.add(new NewBean("上海国际车展十大概念车盘点", "5分钟前", 302,
                "汽车之家", 2, Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("春季科技展重磅产品合集", "3小时前", 567,
                "极客公园", 2, Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("世界地球日公益宣传活动", "1天前", 178,
                "环保在线", 2, Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground)));

        // 更多混合数据...
        newsList.add(new NewBean("人工智能伦理指南白皮书发布", "今天", 211,
                "AI研究院", 1, Arrays.asList(R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("马拉松比赛精彩瞬间集锦", "4小时前", 89,
                "体育周刊", 2, Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("新研究发现咖啡可延长寿命", "昨天", 653,
                "健康生活", 1, Arrays.asList(R.drawable.ic_launcher_foreground)));

        newsList.add(new NewBean("五一假期旅游热门目的地", "30分钟前", 422,
                "旅行家", 2, Arrays.asList(
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground,
                R.drawable.ic_launcher_foreground)));

        NewsAdapter newsAdapter = new NewsAdapter(this, newsList);
        binding.recyclerNews.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerNews.setAdapter(newsAdapter);
    }

    class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NewBean> NewsList;
        private Context context;
        public NewsAdapter(Context context, List<NewBean> list){
            this.context = context;
            this.NewsList = list;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if(viewType == 1){
                NewItem1Binding binding1 = NewItem1Binding.inflate(inflater, parent, false);
                return new MyViewHolder1(binding1);
            }else if(viewType == 2){
                NewItem2Binding binding2 = NewItem2Binding.inflate(inflater, parent, false);
                return new MyViewHolder2(binding2);
            }else{
                return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            NewBean item = NewsList.get(position);
            if(item.type == 1){
                ((MyViewHolder1)holder).bind(item);
            }else if(item.type == 2){
                ((MyViewHolder2)holder).bind(item);
            }else{
                Log.e("mieye", "Adapter:错误item");
            }
        }

        @Override
        public int getItemCount() {
            return NewsList.size();
        }

        @Override
        public int getItemViewType(int i){
            return NewsList.get(i).type;
        }

        class MyViewHolder1 extends RecyclerView.ViewHolder{
            NewItem1Binding binding;
            public MyViewHolder1(@NonNull NewItem1Binding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            @SuppressLint("SetTextI18n")
            public void bind(NewBean item){
                binding.txtNewsTitle.setText(item.title);
                binding.txtNewsUser.setText("作者：" + item.name + " 评论：" + item.comment + " 时间：" + item.time);
                if(item.imgList != null && !item.imgList.isEmpty()){
                    binding.imageView.setBackgroundResource(item.imgList.get(0));
                }

            }
        }

        class MyViewHolder2 extends RecyclerView.ViewHolder{
            NewItem2Binding binding;
            public MyViewHolder2(@NonNull NewItem2Binding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            @SuppressLint("SetTextI18n")
            public void bind(NewBean item){
                binding.txtNewsTitle.setText(item.title);
                binding.txtNewsUser.setText("作者：" + item.name + " 评论：" + item.comment + " 时间：" + item.time);
                ImageView[] viewlist = {binding.imageView1, binding.imageView2, binding.imageView3};
                for(int i = 0; i < item.imgList.size();i++){
                    viewlist[i].setBackgroundResource(item.imgList.get(i));
                }
            }
        }
    }
}