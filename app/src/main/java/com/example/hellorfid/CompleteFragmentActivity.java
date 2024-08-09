package com.example.hellorfid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompleteFragmentActivity  extends Fragment {

    private RecyclerView recyclerView;
    private CompleteFragmentActivity.CompleteTableAdapter tableAdapter;
    private List<CompleteFragmentActivity.CompleteTableRow> tableData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_complete_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize table data
        tableData = new ArrayList<>();
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("CXSBUR449", "20"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("CITSMD449", "40"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));
        tableData.add(new CompleteFragmentActivity.CompleteTableRow("KDFJ329FS", "50"));

        // Add more rows as needed

        // Set up the RecyclerView
        tableAdapter = new CompleteFragmentActivity.CompleteTableAdapter(tableData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tableAdapter);

        return view;
    }

    // Data Model Class
    static class CompleteTableRow {
        private String column1;
        private String column2;

        public CompleteTableRow(String column1, String column2) {
            this.column1 = column1;
            this.column2 = column2;
        }

        public String getColumn1() {
            return column1;
        }

        public String getColumn2() {
            return column2;
        }
    }

    // Adapter Class
    static class CompleteTableAdapter extends RecyclerView.Adapter<CompleteFragmentActivity.CompleteTableAdapter.TableViewHolder> {

        private List<CompleteFragmentActivity.CompleteTableRow> tableData;

        public CompleteTableAdapter(List<CompleteFragmentActivity.CompleteTableRow> tableData) {
            this.tableData = tableData;
        }

        @NonNull
        @Override
        public CompleteFragmentActivity.CompleteTableAdapter.TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbound_complete_item, parent, false);
            return new CompleteFragmentActivity.CompleteTableAdapter.TableViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CompleteFragmentActivity.CompleteTableAdapter.TableViewHolder holder, int position) {
            CompleteFragmentActivity.CompleteTableRow row = tableData.get(position);
            holder.column1.setText(row.getColumn1());
            holder.column2.setText(row.getColumn2());
        }

        @Override
        public int getItemCount() {
            return tableData.size();
        }

        static class TableViewHolder extends RecyclerView.ViewHolder {
            TextView column1;
            TextView column2;

            public TableViewHolder(@NonNull View itemView) {
                super(itemView);
                column1 = itemView.findViewById(R.id.column1);
                column2 = itemView.findViewById(R.id.column2);
            }
        }
    }
}