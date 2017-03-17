package com.onepointglobal.mysurveysn;

import java.util.ArrayList;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.opg.sdk.models.OPGSurvey;


/**
 * The type Survey adapter.
 */
public class SurveyAdapter extends ArrayAdapter<OPGSurvey>
{
   private  Context context;
   private ArrayList<OPGSurvey> opgSurveys;

    /**
     * Instantiates a new Survey adapter.
     *
     * @param context    the context
     * @param opgSurveys the opg surveys
     */
    public SurveyAdapter(Context context, ArrayList<OPGSurvey> opgSurveys)
    {
        super(context, 0, opgSurveys);
        this.context = context;
        this.opgSurveys = opgSurveys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        OPGSurvey surveys = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_surveys, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView surveyid = (TextView) convertView.findViewById(R.id.surveyid);
        TextView updateddate = (TextView) convertView.findViewById(R.id.lastupdated);
        ImageView imv  = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView osOnline = (TextView) convertView.findViewById(R.id.isOnline);
        // Populate the data into the template view using the data object
        name.setText(surveys.getName());
        description.setText(surveys.getDescription());
        surveyid.setText(surveys.getSurveyReference());
        //updateddate.setText(surveys.getLastUpdatedDate());
        imv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bkgnd));
        osOnline.setText("IsOffLine : "+surveys.isOffline());
        // Return the completed view to render on screen
        return convertView;
    }
}