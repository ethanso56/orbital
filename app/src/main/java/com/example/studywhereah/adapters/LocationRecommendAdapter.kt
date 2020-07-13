package com.example.studywhereah.adapters

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.studywhereah.R
import com.example.studywhereah.activities.MapsActivity
import com.example.studywhereah.constants.Constants
import com.example.studywhereah.models.LocationModel

class LocationRecommendAdapter(
    context: Context, resource: Int,
    data: ArrayList<LocationModel>
) : ArrayAdapter<LocationModel>(context, resource, data) {

    private var dataSet : ArrayList<LocationModel>
    var mContext: Context

    init {
        dataSet = data
        mContext = context
    }

    // inner class to contain the views within each row item
    class ViewHolder {
        lateinit var title: TextView
        lateinit var address: TextView
        lateinit var pNum: TextView
        lateinit var oHours: TextView
        lateinit var imgView: ImageView
    }

    // first we try using the position as the tag, to increase code efficiency.


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return super.getView(position, convertView, parent)
        var lModel = dataSet.get(position)
        var convertViewCopy = convertView
        //viewholder is a cache?!
        lateinit var viewHolder: ViewHolder
        lateinit var result: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            var inflater = LayoutInflater.from(mContext)
            convertViewCopy = inflater.inflate(R.layout.row_item, parent, false)
            viewHolder.title = convertViewCopy.findViewById(R.id.tv_locationTitle)
            viewHolder.address = convertViewCopy.findViewById(R.id.tv_locationAddress)
            viewHolder.pNum = convertViewCopy.findViewById(R.id.tv_phoneNum)
            viewHolder.oHours = convertViewCopy.findViewById(R.id.tv_operatingHours)
            viewHolder.imgView = convertViewCopy.findViewById(R.id.iv_location_photo)

            result = convertViewCopy
            convertViewCopy.setTag(lModel)
        } else {
            //maybe can remove and setTag to viewHolder
            viewHolder = ViewHolder()
            viewHolder.title = convertView.findViewById(R.id.tv_locationTitle)
            viewHolder.address = convertView.findViewById(R.id.tv_locationAddress)
            viewHolder.pNum = convertView.findViewById(R.id.tv_phoneNum)
            viewHolder.oHours = convertView.findViewById(R.id.tv_operatingHours)
            viewHolder.imgView = convertView.findViewById(R.id.iv_location_photo)
            result = convertView
            convertView.setTag(lModel)
        }

        viewHolder.title.setText(lModel.getName())
        viewHolder.address.setText(lModel.getAddress())
        val phoneNum = lModel.getPhoneNum()
        if (phoneNum == -1) {
            viewHolder.pNum.setText("• Phone number: Not Available")
        } else {
            viewHolder.pNum.setText("• Phone number: " + phoneNum.toString())
        }
        val operatingHours = lModel.getOperatingHours()
        val openTime = operatingHours[0]
        val closeTime = operatingHours[1]
        viewHolder.oHours.setText("• Operating hours: " + openTime +" to " + closeTime)
//        viewHolder.imgView.setImageResource(......)
//        viewHolder.title.setOnClickListener(this)
        viewHolder.imgView.setTag(position)

        result.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(mContext.applicationContext, MapsActivity::class.java)
                val lModel = v?.tag as LocationModel
                Log.e("Entered onClick", lModel.getName())
                intent.putExtra(Constants.NAMEOFLOCATION, lModel.getName())
                intent.putExtra(Constants.LATITUDEOFLOCATION, lModel.getLatitude())
                intent.putExtra(Constants.LONGITUDEOFLOCATION, lModel.getLongitude())
                intent.putExtra(Constants.ADDRESSOFLOCATION, lModel.getAddress())
                intent.putExtra(Constants.IMAGESOFLOCATION, lModel.getImages())
                intent.putExtra(Constants.OPERATINGHOURS, lModel.getOperatingHours())
                intent.putExtra(Constants.PHONENUMBER, lModel.getPhoneNum())
                intent.putExtra(Constants.FOODAVAILABLE, lModel.getFoodAvailable())
                intent.putExtra(Constants.CHARGINGPORTS, lModel.getChargingPorts())
                //Line below is to tell MapsActivity when Mapactivity was launched from locationsRecommendedActivity
                intent.putExtra("CALLINGACTIVITY", "LocationsRecommendedActivity")
                mContext.applicationContext.startActivity(intent)
            }

        })

        return result
    }
}