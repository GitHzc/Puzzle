package com.example.puzzle.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.puzzle.R;
import com.example.puzzle.model.RankBean;
import com.example.puzzle.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Rank extends AppCompatActivity implements View.OnClickListener {
    private List rankItems;
    private Spinner difficulty;
    private ListView rankList;
    private ImageButton rank_back_button;
    private RankAdapter rankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        rankItems = new ArrayList<RankItem>();
        difficulty = findViewById(R.id.rank_difficulty);
        rankList = findViewById(R.id.rank_list);
        rankList.setEnabled(false);
        rank_back_button = findViewById(R.id.rank_back);
        rank_back_button.setOnClickListener(this);


        Retrofit retrofit = HttpUtils.getRetrofit();
        HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
        api.getAllRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(RankBean rankBean) {
                        List<RankBean.ContentBean> contentBeans = rankBean.getContent();
                        int index = 0;
                        for (RankBean.ContentBean contentBean : contentBeans) {
                            index++;
                            String name = contentBean.getUsername();
                            String mode = contentBean.getMode() + "×" + contentBean.getMode();
                            String score = ScoreToTime(contentBean.getScore());
                            rankItems.add(new RankItem(String.valueOf(index), name, mode, score));
                        }
                        rankAdapter = new RankAdapter(Rank.this, rankItems);
                        rankList.setAdapter(rankAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

        difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Retrofit retrofit = HttpUtils.getRetrofit();
                    HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
                    api.getAllRecord()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<RankBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(RankBean rankBean) {
                                    List<RankBean.ContentBean> contentBeans = rankBean.getContent();
                                    rankItems.clear();
                                    rankAdapter.notifyDataSetChanged();
                                    int index = 0;
                                    for (RankBean.ContentBean contentBean : contentBeans) {
                                        index++;
                                        String name = contentBean.getUsername();
                                        String mode = contentBean.getMode() + "×" + contentBean.getMode();
                                        String score = ScoreToTime(contentBean.getScore());
                                        rankItems.add(new RankItem(String.valueOf(index), name, mode, score));
                                    }
                                    rankAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                } else if (position == 1 || position == 2 || position == 3 || position == 4) {
                    String mode = position == 1 ? "3" : (position == 2 ? "4" : (position == 3 ? "5" : "6"));
                    Retrofit retrofit = HttpUtils.getRetrofit();
                    HttpUtils.Myapi api = retrofit.create(HttpUtils.Myapi.class);
                    api.getRecordWithMode(mode)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<RankBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(RankBean rankBean) {
                                    List<RankBean.ContentBean> contentBeans = rankBean.getContent();
                                    rankItems.clear();
                                    rankAdapter.notifyDataSetChanged();
                                    int index = 0;
                                    if (contentBeans == null) return;
                                    for (RankBean.ContentBean contentBean : contentBeans) {
                                        index++;
                                        String name = contentBean.getUsername();
                                        String mode = contentBean.getMode() + "×" + contentBean.getMode();
                                        String score = ScoreToTime(contentBean.getScore());
                                        rankItems.add(new RankItem(String.valueOf(index), name, mode, score));
                                    }
                                    rankAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                } else {
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String ScoreToTime(String score) {
        Integer scoreInt = Integer.parseInt(score);
        String second = scoreInt % 60 < 10 ? "0" + String.valueOf(scoreInt % 60) : String.valueOf(scoreInt % 60);
        String minute = scoreInt / 60 < 10 ? "0" + String.valueOf(scoreInt / 60) : String.valueOf(scoreInt / 60);
        return minute + ":" + second;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rank_back:
                Rank.this.finish();
                break;
            default:
                break;
        }
    }
}


class RankItem {
    private String rank;
    private String mode;
    private String username;
    private String score;

    public RankItem(String rank, String username, String mode, String score) {
        this.rank = rank;
        this.mode = mode;
        this.username = username;
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public String getMode() {
        return mode;
    }

    public String getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class ViewHolder {
    TextView rank_rank, rank_name, rank_type, rank_score;
}

class RankAdapter extends BaseAdapter {
    List<RankItem> list;
    LayoutInflater inflater;

    public RankAdapter(Context context, List<RankItem> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(list.get(position).getRank()) <= 3 ? 1 : 2;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void update(int index, ListView listview) {
        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        ViewHolder holder = (ViewHolder) view.getTag();
        setData(holder, index);
    }

    private void setData(ViewHolder holder, int index) {
        RankItem rankItem = list.get(index);
        holder.rank_rank.setText(rankItem.getRank());
        holder.rank_name.setText(rankItem.getUsername());
        holder.rank_type.setText(rankItem.getMode());
        holder.rank_score.setText(rankItem.getScore());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        int itemType = this.getItemViewType(position);

        //强制重绘所有Item
        if (itemType == 1) {
            convertView = inflater.inflate(R.layout.rank_item_orange, null);
        } else {
            convertView = inflater.inflate(R.layout.rank_item_white, null);
        }
        viewHolder = new ViewHolder();
        viewHolder.rank_rank = convertView.findViewById(R.id.rank_rank);
        viewHolder.rank_name = convertView.findViewById(R.id.rank_name);
        viewHolder.rank_type = convertView.findViewById(R.id.rank_type);
        viewHolder.rank_score = convertView.findViewById(R.id.rank_score);
        convertView.setTag(viewHolder);

        viewHolder.rank_rank.setText(list.get(position).getRank());
        viewHolder.rank_name.setText(list.get(position).getUsername());
        viewHolder.rank_type.setText(list.get(position).getMode());
        viewHolder.rank_score.setText(list.get(position).getScore());
        return convertView;
    }

}





