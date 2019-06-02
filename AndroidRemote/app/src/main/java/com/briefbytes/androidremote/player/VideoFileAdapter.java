package com.briefbytes.androidremote.player;

import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.briefbytes.androidremote.R;
import com.briefbytes.remoteplayer.client.VideoFile;
import com.briefbytes.remoteplayer.client.action.PlayerAction;
import com.briefbytes.remoteplayer.client.action.PlayerActionPause;
import com.briefbytes.remoteplayer.client.action.PlayerActionPlayFile;
import com.briefbytes.remoteplayer.client.action.PlayerActionToggleSubtitles;

import java.util.ArrayList;
import java.util.List;

public class VideoFileAdapter extends RecyclerView.Adapter<VideoFileAdapter.ViewHolder> {

    private List<VideoFile> files;
    private VideoFileClickListener listener;

    public VideoFileAdapter(VideoFileClickListener listener) {
        files = new ArrayList<>();
        this.listener = listener;
    }

    public void updateFiles(List<VideoFile> list) {
        files.clear();
        files.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        files.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public interface VideoFileClickListener {
        void onClick(VideoFile file, PlayerAction action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView pathView;
        public TextView sizeView;

        public ViewHolder(View itemView) {
            super(itemView);
            pathView = (TextView) itemView.findViewById(R.id.file_path);
            sizeView = (TextView) itemView.findViewById(R.id.file_size);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.play_file).setOnClickListener(this);
            itemView.findViewById(R.id.pause_video).setOnClickListener(this);
            itemView.findViewById(R.id.toggle_subtitles).setOnClickListener(this);
        }

        public void bind(int pos) {
            VideoFile videoFile = files.get(pos);
            pathView.setText(videoFile.getPath());
            sizeView.setText(Formatter.formatFileSize(itemView.getContext(), videoFile.getSize()));
        }

        @Override
        public void onClick(View v) {
            VideoFile videoFile = files.get(getAdapterPosition());
            PlayerAction action;
            switch (v.getId()) {
                case R.id.pause_video:
                    action = new PlayerActionPause();
                    break;
                case R.id.toggle_subtitles:
                    action = new PlayerActionToggleSubtitles();
                    break;
                default:
                    action = new PlayerActionPlayFile();
                    break;
            }
            listener.onClick(videoFile, action);
        }
    }
}
