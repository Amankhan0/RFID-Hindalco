package com.example.hellorfid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InprogressFragmentActivity extends Fragment {

    private RecyclerView recyclerView;
    private InProgressTableAdapter tableAdapter;
    private List<InProgressTableRow> tableData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inprogress_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize table data
        tableData = new ArrayList<>();
        tableData.add(new InProgressTableRow("CXSBUR449", "20"));
        tableData.add(new InProgressTableRow("CITSMD449", "40"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));
        tableData.add(new InProgressTableRow("KDFJ329FS", "50"));

        // Add more rows as needed

        // Set up the RecyclerView
        tableAdapter = new InProgressTableAdapter(tableData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tableAdapter);

        return view;
    }

    // Data Model Class
    static class InProgressTableRow {
        private String column1;
        private String column2;

        public InProgressTableRow(String column1, String column2) {
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
    static class InProgressTableAdapter extends RecyclerView.Adapter<InProgressTableAdapter.TableViewHolder> {

        private List<InProgressTableRow> tableData;

        public InProgressTableAdapter(List<InProgressTableRow> tableData) {
            this.tableData = tableData;
        }

        @NonNull
        @Override
        public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_row, parent, false);
            return new TableViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
            InProgressTableRow row = tableData.get(position);
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
