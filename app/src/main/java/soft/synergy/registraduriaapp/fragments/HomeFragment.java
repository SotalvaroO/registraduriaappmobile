package soft.synergy.registraduriaapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import soft.synergy.registraduriaapp.R;


public class HomeFragment extends Fragment {

    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, container, false);

        ImageSlider imageSlider = mView.findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.reg2,"Bienvenido", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.reg3,"Llevamos el control de las votaciones", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.reg1,"Monitoreo de calidad", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.reg4,"Sondeo de votaciones", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        return mView;
    }
}