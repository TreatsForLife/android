package org.treatsforlife.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.treatsforlife.app.R;
import org.treatsforlife.app.entities.Pet;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PetListAdapter extends ArrayAdapter<Pet> {

    final Context mContext;
    final LayoutInflater mInflater;
    final List<Pet> mPets;

    public PetListAdapter(Context context, List<Pet> pets) {
        super(context, 0, pets);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mPets = pets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.row_pet, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Pet pet = getItem(position);
        if (pet != null) {
            if (pet.media != null && !pet.media.isEmpty())
                Ion.with(holder.image).animateIn(R.anim.fadein).load(pet.media.get(0).url);

            holder.name.setText(pet.name);
        }
        return convertView;
    }

    @Override
    public Pet getItem(int position) {
        return (null != mPets && mPets.size() > position) ? mPets.get(position) : null;
    }

    @Override
    public int getCount() {
        return (null != mPets) ? mPets.size() : 0;
    }

    static class ViewHolder {
        @InjectView(R.id.ivPetRow) ImageView image;
        @InjectView(R.id.tvPetRowName) TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
