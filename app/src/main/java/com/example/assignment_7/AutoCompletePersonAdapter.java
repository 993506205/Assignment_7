package com.example.assignment_7;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AutoCompletePersonAdapter extends ArrayAdapter<Person> {
    private List<Person> personListFull;
    private int m_fontSize;
    private String m_color;
    private Typeface m_typeface;

    public AutoCompletePersonAdapter(@NonNull Context context, @NonNull List<Person> personList, int fontSize, String text_color, Typeface typeface) {
        super(context,0, personList);
        personListFull = new ArrayList<Person>(personList);
        m_fontSize = fontSize;
        m_color = text_color;
        m_typeface = typeface;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return personFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.person_autocomplete_row, parent, false
            );
        }

        TextView tv_lname = convertView.findViewById(R.id.tv_lname);
        TextView tv_fname = convertView.findViewById(R.id.tv_fname);
        TextView tv_phone = convertView.findViewById(R.id.tv_phone);
        TextView tv_hobbies = convertView.findViewById(R.id.tv_hobbies);
        TextView tv_education = convertView.findViewById(R.id.tv_education);

        tv_lname.setTextSize(m_fontSize);
        tv_fname.setTextSize(m_fontSize);
        tv_phone.setTextSize(m_fontSize);
        tv_hobbies.setTextSize(m_fontSize);
        tv_education.setTextSize(m_fontSize);

        tv_lname.setTextColor(Color.parseColor(m_color));
        tv_fname.setTextColor(Color.parseColor(m_color));
        tv_phone.setTextColor(Color.parseColor(m_color));
        tv_hobbies.setTextColor(Color.parseColor(m_color));
        tv_education.setTextColor(Color.parseColor(m_color));

        tv_lname.setTypeface(m_typeface);
        tv_fname.setTypeface(m_typeface);
        tv_phone.setTypeface(m_typeface);
        tv_hobbies.setTypeface(m_typeface);
        tv_education.setTypeface(m_typeface);

        Person person = getItem(position);

        if(person != null){
            tv_lname.setText(person.getSecond_name());
            tv_fname.setText(person.getFirst_name());
            tv_phone.setText(person.getPhone());
            tv_hobbies.setText(person.getHobbies());
            tv_education.setText(person.getEducation());
        }

        return convertView;
    }

    private Filter personFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Person> suggestions = new ArrayList<Person>();

            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(personListFull);
            }else{
                String filerPattern = constraint.toString().toLowerCase().trim();
                for (Person item : personListFull){
                    String snameAndfname = item.getSecond_name().toLowerCase() + " " + item.getFirst_name().toLowerCase();
                    if(item.getFirst_name().toLowerCase().contains(filerPattern)
                    || item.getSecond_name().toLowerCase().contains(filerPattern)
                    || item.getPhone().toLowerCase().contains(filerPattern)
                    || item.getEducation().toLowerCase().contains(filerPattern)
                    || item.getHobbies().toLowerCase().contains(filerPattern)
                    || snameAndfname.contains(filerPattern)){
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Person) resultValue).getSecond_name() + " " +((Person) resultValue).getFirst_name();
        }
    };
}
